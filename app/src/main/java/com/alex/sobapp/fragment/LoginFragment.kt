package com.alex.sobapp.fragment

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.activity.MainActivity
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.ActivityLoginBinding
import com.alex.sobapp.databinding.FragmentLoginBinding
import com.alex.sobapp.utils.AppMd5Utils
import com.alex.sobapp.utils.ClickHelper
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.LoginViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val sHandler = BaseApplication.getHandler()
    private var canLogin = false

    private val loginViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            LoginViewModel::class.java
        )
    }

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        //获取图灵验证码
        loginViewModel.getCaptcha()
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
    }

    override fun initObserver() {
        super.initObserver()
        loginViewModel.apply {
            captchaData.observe(this@LoginFragment, Observer {
                //显示图灵验证码
                Glide.with(this@LoginFragment)
                    .load(it)//it:图灵验证码的获取地址
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//图片不需要缓存
                    .into(binding.verifyCodeImage)
            })
            loginResponse.observe(this@LoginFragment, Observer {
                //Log.d("myLog", "code is ${it.code}")
                //Log.d("myLog", "message is ${it.message}")
                when (it.code) {
                    10000 -> {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        val shpEdit = BaseApplication.getShpEdit()
                        shpEdit!!.putBoolean(Constants.IS_LOGIN, true);
                        shpEdit.apply()
                        val handler = BaseApplication.getHandler()
                        handler!!.postDelayed({
                            activity?.finish()
                        }, 2000)
                    }
                    else -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

    override fun initListener() {
        super.initListener()
        binding.userPasswordEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { text ->
                    if (text.isNotEmpty()) {
                        binding.showPassword.visibility = View.VISIBLE
                    } else {
                        binding.showPassword.visibility = View.GONE
                    }
                }
            }

        })
        binding.userPasswordEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.showPassword.visibility = View.GONE
            } else {
                if (binding.userPasswordEdit.text.isNotEmpty()) {
                    binding.showPassword.visibility = View.VISIBLE
                }
            }
        }
        binding.showPassword.setOnClickListener {
            binding.userPasswordEdit.apply {
                transformationMethod = HideReturnsTransformationMethod.getInstance()
                setSelection(text.length)
                sHandler!!.postDelayed({
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    setSelection(text.length)
                }, 1500)
            }

        }
        binding.loginBtn.setOnClickListener {
            //登录
            //判空
            checkInputIsEmpty()
            if (!ClickHelper.isFastDoubleClick()) {
                val userName: String = binding.userAccountEdit.text.toString().trim()
                val password: String = binding.userPasswordEdit.text.toString().trim()
                val verifyCode: String = binding.userVerifyCodeEdit.text.toString().trim()
                if (canLogin) {
                    loginViewModel.login(userName, AppMd5Utils.MD5(password), verifyCode)
                }
            }
        }
        binding.verifyCodeImage.setOnClickListener {
            //获取新的验证码
            if (!ClickHelper.isFastDoubleClick()) {
                loginViewModel.getCaptcha()
            }

        }
        binding.registerAccount.setOnClickListener {
            //注册
            findNavController().navigate(R.id.toRegisterFragment)
        }
        binding.forgetPassword.setOnClickListener {
            //忘记密码
            findNavController().navigate(R.id.toForgetFragment)
        }
    }

    private fun checkInputIsEmpty() {
        binding.apply {
            val accountEmpty = userAccountEdit.text?.isEmpty()!!
            val passwordEmpty = userPasswordEdit.text?.isEmpty()!!
            val verifyCodeEmpty = userVerifyCodeEdit.text?.isEmpty()!!
            when {
                accountEmpty -> {
                    Toast.makeText(requireContext(), "请输入手机号", Toast.LENGTH_SHORT).show()
                }
                passwordEmpty -> {
                    Toast.makeText(requireContext(), "请输入密码", Toast.LENGTH_SHORT).show()
                }
                verifyCodeEmpty -> {
                    Toast.makeText(requireContext(), "请输入验证码", Toast.LENGTH_SHORT).show()
                }
            }
            canLogin = !accountEmpty && !passwordEmpty && !verifyCodeEmpty

        }
    }
}