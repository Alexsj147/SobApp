package com.alex.sobapp.utils

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.concurrent.ConcurrentHashMap

/**
 * cookie的管理类
 */
class CookiesManager : CookieJar {

    private val cookieStoreBlog = HashMap<String, List<Cookie>>()

    override fun saveFromResponse(httpUrl: HttpUrl, list: List<Cookie>) {
        Log.d("myLog", "Response httpUrl:$httpUrl")
        if (Constants.BASE_URL.contains(httpUrl.host)) {
            cookieStoreBlog[Constants.BASE_URL] = list
            Log.d("myLog", "保存sob的数据,$list")
        }
    }

    override fun loadForRequest(httpUrl: HttpUrl): List<Cookie> {

        val list = ArrayList<Cookie>()
        val sob = cookieStoreBlog[Constants.BASE_URL];
        sob?.apply {
            Log.d("返回sob的cookie", sob.toString())
            return this
        }
        return list
    }
}