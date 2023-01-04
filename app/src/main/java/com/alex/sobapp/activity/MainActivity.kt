package com.alex.sobapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alex.sobapp.R
import com.alex.sobapp.base.BaseActivity
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.custom.CircleImageView
import com.alex.sobapp.databinding.ActivityMainBinding
import com.alex.sobapp.viewmodel.CheckTokenViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val checkTokenViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(CheckTokenViewModel::class.java)
    }

    private val shp = BaseApplication.getShp()
    private val handler = BaseApplication.getHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragment_main_activity)
        binding.mainBottomNav.setupWithNavController(navController)

        //ImmersionBar.with(this)
        //    .statusBarDarkFont(true)
        //    .init()
        //token解析
        initObserver()
        initListener()
    }

    private fun initListener() {
        val shp = BaseApplication.getShp()
        val userAvatar = shp?.getString("userAvatar", "")
        val menuView = binding.mainBottomNav.getChildAt(0) as BottomNavigationMenuView
        //Log.d("myLog", "count is ${menuView.childCount}")
        val item = menuView.getChildAt(menuView.childCount-1) as BottomNavigationItemView
        val imageView: ImageView =
            item.findViewById(com.google.android.material.R.id.icon)
        if (!userAvatar.equals("")) {
            Glide.with(this)
                .load(userAvatar)
                .apply(RequestOptions.bitmapTransform(MultiTransformation(CircleCrop())))
                .into(imageView)
        }


    }


    override fun onResume() {
        super.onResume()
        checkTokenViewModel.tokenCheck()
    }

    private fun initObserver() {
        //val isLogin = shp!!.getBoolean("isLogin", false)
        //if (!isLogin) {
        checkTokenViewModel.apply {
            resultToken.observe(this@MainActivity, Observer {
                println("返回的token is  $it")
                if (it.success) {
                    if (it.data != null) {
                        val shpEdit = BaseApplication.getShpEdit()
                        shpEdit!!.putString("userId", it.data.id)
                        shpEdit.putString("userAvatar", it.data.avatar)
                        shpEdit.putString("userNickName", it.data.nickname)
                        shpEdit.apply()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "账号未登录，即将跳转至登录界面", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    handler?.postDelayed(Runnable {
                        startActivity(intent)
                        this@MainActivity.finish()
                    }, 2000)
                }

            })
        }
        //}
    }
}