package com.alex.sobapp.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.alex.sobapp.R


class PausePressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mLayoutParams: ViewGroup.LayoutParams? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var rect: RectF? = null

    //倒计时的时长
    private val countDownDuration = 2

    //当前的进度
    private var currentProgress = 0f

    //手是否有释放
    private var isRelease = false
    private var mOnCountDownStateChangeListener: OnCountDownStateChangeListener? =
        null
    private var paint: Paint? = null

    init {
        initPaint()
        setPadding(10, 10, 10, 10)
    }

    private fun initPaint() {
        paint = Paint()
        paint?.let {
            it.color = context.resources.getColor(R.color.ffff6e66);
            it.style = Paint.Style.STROKE
            it.isAntiAlias = true
            it.strokeCap = Paint.Cap.ROUND
            it.strokeWidth = 10f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mLayoutParams = this.layoutParams
        if (mLayoutParams != null) {
            mWidth = layoutParams.width
            mHeight = layoutParams.height
        }
        //获取到图片资源的大小，然后设置大小
        setMeasuredDimension(mWidth, mHeight)
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        if (rect == null) {
            rect = RectF(10f, 10f, (measuredWidth - 10).toFloat(), (measuredHeight - 10).toFloat())
        }
        paint?.color = context.resources.getColor(R.color.ffffe7e6)
        //绘制底线
        canvas?.drawArc(rect!!, -90f, 360f, false, paint!!)
        paint?.color = context.resources.getColor(R.color.ffff6e66);
        //绘制前景线
        canvas?.drawArc(rect!!, -90f, currentProgress, false, paint!!)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                //开始倒计时
                this.isRelease = false
                startCountDown()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                performClick()
                //结束倒计时
                this.isRelease = true
                //告诉外部
                if (currentProgress < 360 && mOnCountDownStateChangeListener != null) {
                    mOnCountDownStateChangeListener?.onCountDownCancel()
                }
            }
            else -> {

            }
        }
        //消费事件
        return true
    }

    fun setOnCountDownStateChangeListener(listener: OnCountDownStateChangeListener) {
        this.mOnCountDownStateChangeListener = listener
    }

    interface OnCountDownStateChangeListener {
        //倒计时结束
        fun onCountDownEnd()

        //用户取消倒计时
        fun onCountDownCancel()
    }

    /**
     * 开始倒计时
     */
    private fun startCountDown() {
        if (currentProgress >= 360) {
            currentProgress = 0f
        }
        //换算成毫秒
        val duration: Int
        duration = if (!isRelease) {
            countDownDuration * 1000
        } else {
            //往回倒时只要1秒的动画即可
            1000
        }
        //第20毫秒绘制一次进度
        val rate = duration / 20
        //求度数
        val perDegree = 360 * 1.0f / rate
        post(object : Runnable {
            override fun run() {
                if (!isRelease) {
                    currentProgress += perDegree
                } else {
                    currentProgress -= perDegree
                }
                if (currentProgress < 0) {
                    currentProgress = 0f
                }
                if (currentProgress >= 360) {
                    //结束了
                    currentProgress = 360f
                    //告诉外部
                    if (mOnCountDownStateChangeListener != null) {
                        mOnCountDownStateChangeListener!!.onCountDownEnd()
                    }
                }
                //重新绘制
                invalidate()
                if (currentProgress > 0 && currentProgress < 360) {
                    postDelayed(this, 20)
                }
            }
        })
    }

}