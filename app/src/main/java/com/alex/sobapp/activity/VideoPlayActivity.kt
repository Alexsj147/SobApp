package com.alex.sobapp.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.VideoResult
import com.alex.sobapp.base.BaseActivity
import com.alex.sobapp.databinding.ActivityVideoPlayBinding
import com.alex.sobapp.viewmodel.CourseViewModel
import com.alex.sobapp.viewmodel.PlayerStatus
import com.alex.sobapp.viewmodel.PlayerViewModel
import com.aliyun.player.AliPlayer
import com.aliyun.player.AliPlayerFactory
import com.aliyun.player.IPlayer
import com.aliyun.player.source.VidAuth
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.android.synthetic.main.activity_video_play.*
import kotlinx.android.synthetic.main.controller_layout.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat


class VideoPlayActivity : BaseActivity() {

    private lateinit var binding: ActivityVideoPlayBinding
    private var aliPlayer: AliPlayer? = null

    //private val courseViewModel by lazy {
    //    CourseViewModel()
    //}
    private lateinit var courseViewModel: CourseViewModel

    //private val playerViewModel by lazy {
    //    PlayerViewModel(this)
    //}
    private lateinit var playerViewModel: PlayerViewModel
    //private var hasGetVideo: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Log.d("myLog", "onCreate")
        playerViewModel = ViewModelProvider(
            this
        ).get(PlayerViewModel::class.java)
        updatePlayerProgress()
        initEvent()
        //initVideoPlay()
        initView()
        initObserver()
        lifecycle.addObserver(playerViewModel)
        initListener()
    }

    private fun initEvent() {
        courseViewModel = ViewModelProvider(
            this
        ).get(CourseViewModel::class.java)
        //Log.d("myLog", "hasGetVideo is  ${playerViewModel.getHasGetVideoValue()}")
        intent?.let { intent ->
            intent.extras?.let {
                val videoId: String = it.getString("videoId", "")
                if (!playerViewModel.getHasGetVideoValue()) {
                    courseViewModel.loadVideo(videoId)
                    //Log.d("myLog", "??????????????????")
                } else {
                    Log.d("myLog", "???????????????????????????")
                }

            }
        }
    }

    private fun initVideoPlay() {
        Log.d("myLog", "initVideoPlay")
        //???????????????
        aliPlayer = AliPlayerFactory.createAliPlayer(this)
        Log.d("myLog", " aliPlayer is $aliPlayer")
        //??????????????????????????????????????????traceId?????????DisableAnalytics???????????????????????????????????????traceId?????????????????????????????????????????????????????????
        //????????????traceId????????????????????????traceId????????????????????????????????????????????????imei???idfa???
        //aliPlayer.setTraceId("traceId");
        //????????????
        //aliPlayer?.isAutoPlay = true

    }

    private fun updatePlayerProgress() {
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                val currentPosition = playerViewModel.currentPositionValue.value
                binding.controllerFrame.seekBar.progress = currentPosition?.toInt() ?: 0
                //Log.d("myLog", "seekBar max is ${binding.controllerFrame.seekBar.progress}")
            }
        }
    }

    private fun initView() {
        //Log.d("myLog", "initView  aliPlayer is $aliPlayer")
        //Log.d("myLog", "initView playerViewModel is $playerViewModel")
        binding.progressBar.visibility = View.VISIBLE
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                //initVideoPlay()
                playerViewModel.aliPlayer.setSurface(holder.surface)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                playerViewModel.aliPlayer.surfaceChanged()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                playerViewModel.aliPlayer.setSurface(null)
            }

        })
    }

    private fun initObserver() {
        //Log.d("myLog", "initObserver  playerViewModel is $playerViewModel")
        courseViewModel.apply {
            videoValue.observe(this@VideoPlayActivity, Observer {
                //Log.d("myLog","videoResult is $it")
                //initVidAuth(it)
                if (!playerViewModel.getHasGetVideoValue()) {
                    playerViewModel.loadVidAuth(it)
                    playerViewModel.setHasGetVideoValue(true)
                    //Log.d("myLog", "hasGetVideo is  ${playerViewModel.getHasGetVideoValue()}")
                } else {
                    Log.d("myLog", "?????????????????????????????????")
                }
            })
        }
        playerViewModel.apply {
            duration.observe(this@VideoPlayActivity, Observer {
                if (it != 0L) {
                    binding.controllerFrame.seekBar.max = (it.toInt()) / 1000
                    //Log.d("myLog", "observer duration is ${(it.toInt()) / 1000}")
                }
                //binding.controllerFrame.seekBar.max = (aliPlayer.duration.toInt()) / 1000
                //Log.d("myLog", "seekBar max is ${binding.controllerFrame.seekBar.max}")
            })
            videoResolution.observe(this@VideoPlayActivity, Observer {
                //binding.playerFrame.post {
                //    reSizePlayer(it.first, it.second)
                //}
                reSizePlayer()
            })
            bufferedPositionValue.observe(this@VideoPlayActivity, Observer {
                binding.controllerFrame.seekBar.secondaryProgress = it.toInt()
            })
            progressBarVisibility.observe(this@VideoPlayActivity, Observer {
                binding.progressBar.visibility = it
            })
            controllerFrameVisibility.observe(this@VideoPlayActivity, Observer {
                //Log.d("myLog", "current controllerFrameVisibility is $it")
                binding.controllerFrame.controllerFrame.visibility = it
            })
            currentPositionValue.observe(this@VideoPlayActivity, Observer {
                val formatter = SimpleDateFormat("mm:ss") //?????????Formatter??????????????????
                val duration = formatter.format(playerViewModel.aliPlayer.duration)
                val current = formatter.format(it * 1000)
                binding.controllerFrame.progressText.text = "$current:$duration"
            })
            playerStatus.observe(this@VideoPlayActivity, Observer {
                binding.controllerFrame.controllButton.isClickable = true
                when (it) {
                    PlayerStatus.Paused -> {
                        controllButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                    PlayerStatus.Completed -> {
                        controllButton.setImageResource(R.drawable.ic_baseline_replay_24)
                    }
                    PlayerStatus.NotReady -> {
                        controllButton.isClickable = false
                    }
                    else -> {
                        controllButton.setImageResource(R.drawable.ic_baseline_pause_24)
                    }
                }
            })
        }
    }

    private fun reSizePlayer() {
        //???????????????????????????????????????????????????????????????view?????????????????????????????????
        //playerViewModel.aliPlayer.scaleMode = IPlayer.ScaleMode.SCALE_ASPECT_FIT
        //?????????????????????????????????????????????????????????????????????view???????????????????????????
        playerViewModel.aliPlayer.scaleMode = IPlayer.ScaleMode.SCALE_ASPECT_FILL
        //????????????????????????????????????????????????view??????????????????????????????????????????
        //playerViewModel.aliPlayer.scaleMode = IPlayer.ScaleMode.SCALE_TO_FILL
    }

    /**
     * ??????????????????
     */
    private fun rotatePlayer() {
        //???????????????????????????0???
        playerViewModel.aliPlayer.rotateMode = IPlayer.RotateMode.ROTATE_0
        //???????????????????????????90???
        playerViewModel.aliPlayer.rotateMode = IPlayer.RotateMode.ROTATE_90
        //???????????????????????????180???
        playerViewModel.aliPlayer.rotateMode = IPlayer.RotateMode.ROTATE_180
        //???????????????????????????270???
        playerViewModel.aliPlayer.rotateMode = IPlayer.RotateMode.ROTATE_270
        //??????????????????
        playerViewModel.aliPlayer.rotateMode
    }

    /**
     * ??????????????????
     */
    private fun mirrorPlayer() {
        //???????????????
        playerViewModel.aliPlayer.mirrorMode = IPlayer.MirrorMode.MIRROR_MODE_NONE;
        //??????????????????
        playerViewModel.aliPlayer.mirrorMode = IPlayer.MirrorMode.MIRROR_MODE_HORIZONTAL;
        //??????????????????
        playerViewModel.aliPlayer.mirrorMode = IPlayer.MirrorMode.MIRROR_MODE_VERTICAL;
    }


    private fun initVidAuth(videoResult: VideoResult) {
        Log.d("myLog", "initVidAuth")
        val vidAuth = VidAuth()
        vidAuth.vid = videoResult.videoId
        vidAuth.playAuth = videoResult.playAuth
        //?????????????????????????????????????????????cn-shanghai
        vidAuth.region = "cn-shanghai"
        aliPlayer?.setDataSource(vidAuth)
        aliPlayer?.prepare()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                //????????????????????????????????????????????????
                playerViewModel.aliPlayer.stop()
                finish()
            }
        }
        return true

    }

    private fun initListener() {
        //Log.d("myLog", "initListener aliPlayer is $aliPlayer")
        //Log.d("myLog", "initListener  playerViewModel is $playerViewModel")
        binding.controllerFrame.videoBack.setOnClickListener {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                //????????????????????????????????????????????????
                playerViewModel.aliPlayer.stop()
                finish()
            }
        }
        binding.controllerFrame.controllButton.setOnClickListener {
            playerViewModel.togglePlayerStatus()
        }
        binding.playerFrame.setOnClickListener {
            //?????????????????????????????????(????????????)
            playerViewModel.toggleControllerVisibility()
            //Log.d("myLog", "controllerFrameVisibility is ${playerViewModel.controllerFrameVisibility.value}")
        }
        binding.controllerFrame.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                //Log.d("myLog","progress is $progress")
                //?????????????????????????????????
                if (fromUser) {
                    playerViewModel.playerSeekToProgress((progress * 1000).toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }

        })
        binding.controllerFrame.fullScreen.setOnClickListener {
            //???????????????(????????????)
            //Log.d("myLog", "???????????????")
            //???????????????orientation
            requestedOrientation =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }

        }

    }

    /*override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("myLog", "orientation is ${newConfig.orientation}")
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI()
            playerViewModel.emmitVideoResolution()
        }
    }*/

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //Log.d("myLog","playerViewmodel is $playerViewModel")
        //Log.d("myLog","playerViewModel myMediaPlayer is ${playerViewModel.aliPlayer}")
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI()
            playerViewModel.emmitVideoResolution()
        }
    }


    private fun reSizePlayer(width: Int, height: Int) {
        if (width == 0 || height == 0) return
        binding.surfaceView.layoutParams = FrameLayout.LayoutParams(
            binding.playerFrame.height * width / height,
            FrameLayout.LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        )
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

}