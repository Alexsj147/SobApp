package com.alex.sobapp.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.alex.sobapp.apiService.domain.VideoResult
import com.aliyun.player.AliPlayer
import com.aliyun.player.AliPlayerFactory
import com.aliyun.player.IPlayer
import com.aliyun.player.bean.ErrorCode
import com.aliyun.player.bean.ErrorInfo
import com.aliyun.player.bean.InfoBean
import com.aliyun.player.bean.InfoCode
import com.aliyun.player.source.VidAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.alex.sobapp.base.BaseApplication

enum class PlayerStatus {
    Playing, Paused, Completed, NotReady
}

class PlayerViewModel : ViewModel(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pausePlayer() {
        aliPlayer.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumePlayer() {
        aliPlayer.start()
    }

    val aliPlayer: AliPlayer = AliPlayerFactory.createAliPlayer(BaseApplication.getContext())

    //
    private val _progressBarVisibility = MutableLiveData(View.VISIBLE)
    val progressBarVisibility: LiveData<Int> = _progressBarVisibility
    private val _videoResolution = MutableLiveData(Pair(0, 0))
    val videoResolution: LiveData<Pair<Int, Int>> = _videoResolution
    private val _controllerFrameVisibility = MutableLiveData(View.INVISIBLE)
    val controllerFrameVisibility: LiveData<Int> = _controllerFrameVisibility
    private var controllerShowTime = 0L
    private val _bufferPercent = MutableLiveData(0)
    val bufferPercent: LiveData<Int> = _bufferPercent
    private val _duration = MutableLiveData(0L)
    val duration: LiveData<Long> = _duration
    private val _playerStatus = MutableLiveData(PlayerStatus.NotReady)
    val playerStatus: LiveData<PlayerStatus> = _playerStatus
    val currentPositionValue = MutableLiveData<Long>()
    val bufferedPositionValue = MutableLiveData<Long>()
    private var hasGetVideoValue: Boolean = false

    init {
        loadVideo()
    }

    fun setHasGetVideoValue(hasGetVideo: Boolean) {
        hasGetVideoValue = hasGetVideo
    }

    fun getHasGetVideoValue(): Boolean {
        return hasGetVideoValue
    }

    /**
     * 加载aliPlayer
     */
    private fun loadVideo() {
        aliPlayer.apply {
            //埋点日志上报功能默认开启，当traceId设置为DisableAnalytics时，则关闭埋点日志上报。当traceId设置为其他参数时，则开启埋点日志上报。
            //建议传递traceId，便于跟踪日志。traceId为设备或用户的唯一标识符，通常为imei或idfa。
            //aliPlayer.setTraceId("traceId");
            //自动播放
            //isAutoPlay = true
            _progressBarVisibility.value = View.VISIBLE
            setOnPreparedListener(object : IPlayer.OnPreparedListener {
                override fun onPrepared() {
                    _progressBarVisibility.value = View.INVISIBLE
                    _duration.value = aliPlayer.duration
                    //Log.d("myLog", "duration value is ${_duration.value}")
                    //一般调用start开始播放视频
                    this@apply.start()
                    _playerStatus.value = PlayerStatus.Playing
                }
            })
            setOnVideoSizeChangedListener(object : IPlayer.OnVideoSizeChangedListener {
                override fun onVideoSizeChanged(width: Int, height: Int) {
                    _videoResolution.value = Pair(width, height)
                }
            })

            setOnSeekCompleteListener(object : IPlayer.OnSeekCompleteListener {
                override fun onSeekComplete() {
                    aliPlayer.start()
                    _playerStatus.value = PlayerStatus.Playing
                    _progressBarVisibility.value = View.INVISIBLE
                }

            })

            setOnErrorListener(object : IPlayer.OnErrorListener {
                override fun onError(errorInfo: ErrorInfo) {
                    val errorCode: ErrorCode = errorInfo.code //错误码
                    val errorMsg: String = errorInfo.msg //错误描述
                    Log.d("myLog", "errorCode is $errorCode  //  errorMsg is $errorMsg")
                    //出错后需要停止掉播放器
                    this@apply.stop()
                }
            })
            setOnCompletionListener(object : IPlayer.OnCompletionListener {
                //播放完成之后，就会回调到此接口。
                override fun onCompletion() {
                    _playerStatus.value = PlayerStatus.Completed
                    //Log.d("myLog", "onCompletion")
                    //一般调用stop停止播放视频
                    //this@apply.stop()
                }

            })
            setOnInfoListener(object : IPlayer.OnInfoListener {
                override fun onInfo(infoBean: InfoBean?) {
                    //val code: InfoCode? = infoBean?.code //信息码
                    //val msg: String? = infoBean?.extraMsg //信息内容
                    //val value: Long? = infoBean?.extraValue //信息值
                    //val currentPosition = InfoCode.CurrentPosition//当前进度：
                    //val bufferedPosition = InfoCode.BufferedPosition//当前缓存位置：
                    if (infoBean?.code == InfoCode.CurrentPosition) {
                        //extraValue为当前播放进度，单位为毫秒
                        //currentPositionValue.value = infoBean.extraValue
                        currentPositionValue.value = (infoBean.extraValue) / 1000
                        //Log.d("myLog", "currentPositionValue is ${currentPositionValue.value}")
                    }
                    if (infoBean?.code == InfoCode.BufferedPosition) {
                        //extraValue为当前缓存进度，单位为毫秒
                        //bufferedPositionValue.value = infoBean.extraValue
                        bufferedPositionValue.value = (infoBean.extraValue) / 1000
                        //Log.d("myLog", "bufferedPositionValue is ${bufferedPositionValue.value}")
                    }

                }

            })
            setOnLoadingStatusListener(object : IPlayer.OnLoadingStatusListener {
                //播放器的加载状态, 网络不佳时，用于展示加载画面。

                override fun onLoadingBegin() {
                    //开始加载。画面和声音不足以播放。
                    //一般在此处显示圆形加载
                    _progressBarVisibility.value = View.VISIBLE
                }

                override fun onLoadingProgress(percent: Int, netSpeed: Float) {
                    //加载进度。百分比和网速。
                    Log.d("myLog", "percent is $percent")
                    Log.d("myLog", "netSpeed is $netSpeed")
                }

                override fun onLoadingEnd() {
                    //结束加载。画面和声音可以播放。
                    //一般在此处隐藏圆形加载
                    _progressBarVisibility.value = View.INVISIBLE
                }

            })
        }
    }

    /**
     * 控制播放按钮的图标显示
     */
    fun togglePlayerStatus() {
        when (_playerStatus.value) {
            PlayerStatus.Playing -> {
                aliPlayer.pause()
                _playerStatus.value = PlayerStatus.Paused
            }
            PlayerStatus.Paused -> {
                aliPlayer.start()
                _playerStatus.value = PlayerStatus.Playing
            }
            PlayerStatus.Completed -> {
                aliPlayer.start()
                _playerStatus.value = PlayerStatus.Playing
            }
            else -> {
                return
            }
        }
    }

    /**
     * 控制播放控件的显示于隐藏
     */
    fun toggleControllerVisibility() {
        if (_controllerFrameVisibility.value == View.INVISIBLE) {
            _controllerFrameVisibility.value = View.VISIBLE
            controllerShowTime = System.currentTimeMillis()
//            Log.d(
//                "myLog",
//                "click controllerShowTime is $controllerShowTime"
//            )
            viewModelScope.launch {
                delay(3000)
//                Log.d(
//                    "myLog",
//                    "delay controllerShowTime is ${System.currentTimeMillis()}"
//                )
                if (System.currentTimeMillis() - controllerShowTime >= 3000) {
                    _controllerFrameVisibility.value = View.INVISIBLE
//                    Log.d(
//                        "myLog",
//                        "_controllerFrameVisibility is ${_controllerFrameVisibility.value}"
//                    )
                } else {
                    Log.d("myLog", "delay时间未超过3秒")//测试时使用
                }
            }
        } else {
            _controllerFrameVisibility.value = View.INVISIBLE
        }
    }

    fun emmitVideoResolution() {
        _videoResolution.value = _videoResolution.value
    }

    /**
     * progress：拖动后的目标位置，必须是毫秒
     */
    fun playerSeekToProgress(progress: Long) {
        _progressBarVisibility.value = View.VISIBLE
        aliPlayer.seekTo(progress)
    }

    /**
     * 使用VidAuth播放方式播放点播视频
     * 加载配置
     */
    fun loadVidAuth(videoResult: VideoResult) {
        Log.d("myLog", "set video init")
        val vidAuth = VidAuth()
        vidAuth.vid = videoResult.videoId
        vidAuth.playAuth = videoResult.playAuth
        //阳光沙滩的视频接入地域是上海：cn-shanghai
        vidAuth.region = "cn-shanghai"
        aliPlayer.setDataSource(vidAuth)
        aliPlayer.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        aliPlayer.release()
    }
}