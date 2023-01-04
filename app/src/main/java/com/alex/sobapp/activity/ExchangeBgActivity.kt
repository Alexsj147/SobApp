package com.alex.sobapp.activity

import android.os.Bundle
import com.alex.sobapp.R
import com.alex.sobapp.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar

class ExchangeBgActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_bg)
        //ImmersionBar.with(this)
        //    .statusBarDarkFont(true)
        //    .init()
    }
}