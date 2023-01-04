package com.alex.sobapp.utils

class FormatDuration {
    fun getString(t: Long): String {
        var m: String = ""
        m = if (t > 0) {
            if (t < 10) {
                "0$t"
            } else {
                "$t"
            }
        } else {
            "00"
        }
        return m
    }

    fun format(t: Long): String {
        return when {
            t < 60000 -> {
                val time = (t % 60000) / 1000
                "${time}秒"
            }
            t in 60000..359999 -> {
                "${(t % 360000) / 60000}:${(t % 60000) / 1000}"
            }
            else -> {
                "${(t / 360000)}:${(t % 360000) / 60000}:${(t % 60000) / 1000}"
            }
        }
    }
}