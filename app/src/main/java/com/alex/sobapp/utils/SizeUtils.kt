package com.alex.sobapp.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

object SizeUtils {
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
    fun getScreenSize(context: Context): Point {
        val point = Point()
        val systemService =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        systemService.defaultDisplay.getSize(point)
        return point
    }
}