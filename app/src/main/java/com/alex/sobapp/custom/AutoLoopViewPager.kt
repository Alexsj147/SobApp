package com.alex.sobapp.custom

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class AutoLoopViewPager constructor(context: Context, attributeSet: AttributeSet) :
    ViewPager(context, attributeSet) {

    private var isLoop = false

    private val mTask: Runnable = object : Runnable {
        override fun run() {
            var currentItem = currentItem
            currentItem++
            setCurrentItem(currentItem)
            if (isLoop) {
                postDelayed(this, 3000)
            }
        }
    }

    fun startLoop() {
        isLoop = true
        post(mTask)
    }

    fun stopLoop() {
        isLoop = false
        removeCallbacks(mTask)
    }
}