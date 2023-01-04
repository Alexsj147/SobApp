package com.alex.sobapp.utils

import android.util.Log
import com.alex.sobapp.apiService.Api
import com.alex.sobapp.base.BaseApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * retrofit的创建
 */
object RetrofitClient {
    val shp = BaseApplication.getShp()
    val shpEdit = BaseApplication.getShpEdit()
    val okHttpClient = OkHttpClient.Builder()
        .cookieJar(CookiesManager())
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val sobToken = shp?.getString("sob_token", "")
                Log.d("myLog", "sobToken is $sobToken")
                val original = chain.request()
                val request = original.newBuilder()
                    .header("sob_token", sobToken!!)
                    .method(original.method, original.body)
                    .build()
                return chain.proceed(request)
            }
        })
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val response = chain.proceed(chain.request())
                //显示返回的l_c_i
                if (response.header("l_c_i") != null) {
                    Log.d("myLog", "l_c_i is ${response.header("l_c_i")}")
                    shpEdit?.putString("l_c_i", response.header("l_c_i"))
                    shpEdit?.apply()
                }
                if (response.header("sob_token") != null) {
                    Log.d("myLog", "new sob_token is ${response.header("sob_token")}")
                    shpEdit?.putString("sob_token", response.header("sob_token"))
                    shpEdit?.apply()
                }
                return response
            }
        })
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()


    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()


    val apiService = retrofit.create(Api::class.java)
}