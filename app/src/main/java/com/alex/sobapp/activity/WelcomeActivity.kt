package com.alex.sobapp.activity

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.alex.sobapp.R
import com.alex.sobapp.base.BaseActivity
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.databinding.ActivityWelcomeBinding
import com.alex.sobapp.utils.Constants.PERMISSION_REQUEST_CODE
import com.bumptech.glide.Glide

class WelcomeActivity : BaseActivity() {

    private lateinit var binding: ActivityWelcomeBinding


    private val imgList = arrayListOf(
        R.mipmap.by1, R.mipmap.by2, R.mipmap.by3, R.mipmap.by4,
        R.mipmap.yk1, R.mipmap.yk2, R.mipmap.yk3, R.mipmap.yk4,
        R.mipmap.yk5, R.mipmap.yk6, R.mipmap.yk7, R.mipmap.yk8
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
        initView()
        //checkPermission()
        //guide()
    }

    private fun initView() {
        //val showUrl = imgList.random()
        //Glide.with(this).load(showUrl).into(binding.welcomeImg)
    }



    private fun initEvent() {

    }




}