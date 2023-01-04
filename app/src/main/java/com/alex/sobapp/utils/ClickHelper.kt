package com.alex.sobapp.utils

class ClickHelper {
    companion object {
        private var lastClickTime: Long = 0

        /**
         * 判断事件出发时间间隔是否超过预定值
         * 如果小于间隔（目前是1000毫秒）则返回true，否则返回false
         */
        fun isFastDoubleClick(): Boolean {
            val time = System.currentTimeMillis()
            val timeClick = time - lastClickTime
            return if (timeClick in 1..999) {
                true
            } else {
                lastClickTime = time
                false
            }
        }
    }

}