package com.alex.sobapp.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet

class CircleImageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context!!, attrs, defStyleAttr) {
    private val mPaint: Paint = Paint()
    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (null != drawable) {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val bp = getCircleBitmap(bitmap, 14)
            val rectSrc =
                Rect(0, 0, bp.width, bp.height)
            val rectDest =
                Rect(0, 0, width, height)
            mPaint.reset()
            canvas.drawBitmap(bp, rectSrc, rectDest, mPaint)
        } else {
            super.onDraw(canvas)
        }
    }

    private fun getCircleBitmap(bitmap: Bitmap, pixels: Int): Bitmap {
        val output =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val rect =
            Rect(0, 0, bitmap.width, bitmap.height)
        mPaint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        mPaint.color = color
        val x = bitmap.width
        canvas.drawCircle(x / 2.toFloat(), x / 2.toFloat(), x / 2.toFloat(), mPaint)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, mPaint)
        return output
    }

}