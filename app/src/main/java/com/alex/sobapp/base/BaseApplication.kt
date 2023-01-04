package com.alex.sobapp.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import com.alex.sobapp.utils.Constants


class BaseApplication : Application() {

    companion object {
        private var sContext: Context? = null
        private var sHandler: Handler? = null
        private var sharedPreferences: SharedPreferences? = null
        private var sharedPreferencesEdit: SharedPreferences.Editor? = null

        open fun getContext(): Context? {
            return sContext
        }

        open fun getHandler(): Handler? {
            return sHandler
        }

        open fun getShp(): SharedPreferences? {
            return sharedPreferences
        }

        open fun getShpEdit(): SharedPreferences.Editor? {
            return sharedPreferencesEdit
        }
    }

    override fun onCreate() {
        super.onCreate()
        sContext = baseContext
        sHandler = Handler()
        sharedPreferences = getSharedPreferences(Constants.SHP_NAME, Context.MODE_PRIVATE)
        sharedPreferencesEdit =
            getSharedPreferences(Constants.SHP_NAME, Context.MODE_PRIVATE).edit()
        sharedPreferencesEdit?.apply()
    }
}