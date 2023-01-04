package com.alex.sobapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alex.sobapp.R
import com.alex.sobapp.base.BaseActivity
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.databinding.ActivityLoginBinding
import com.alex.sobapp.utils.AppMd5Utils
import com.alex.sobapp.utils.ClickHelper
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.viewmodel.LoginViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}