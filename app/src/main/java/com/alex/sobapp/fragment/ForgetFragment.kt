package com.alex.sobapp.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.SendSmsVo
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentForgetBinding
import com.alex.sobapp.utils.ClickHelper
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.LoginViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ForgetFragment : BaseFragment<FragmentForgetBinding>() {

    private var timeCount: CountDownTimer? = null
    private val loginViewModel by lazy {
        LoginViewModel()
    }
    private var canGetPhoneVerifyCode: Boolean = false
    private var smsCodeIsTrue: Boolean = false

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentForgetBinding {
        return FragmentForgetBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        loginViewModel.getCaptcha()
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
    }

    override fun initObserver() {
        super.initObserver()
        loginViewModel.apply {
            captchaData.observe(this@ForgetFragment, Observer {
                //显示图灵验证码
                Glide.with(this@ForgetFragment)
                    .load(it)//it:图灵验证码的获取地址
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//图片不需要缓存
                    .into(binding.verifyCodeImage)
            })
            checkSmsResult.observe(this@ForgetFragment, Observer {
                //判断验证码是否正确
                smsCodeIsTrue = it.success
                val phoneNum: String = binding.userPhoneEdit.text.toString().trim()
                val smsCode: String = binding.userPhoneVerifyCodeEdit.text.toString().trim()
                val bundle = Bundle()
                bundle.putString("phoneNum", phoneNum)
                bundle.putString("smsCode", smsCode)
                if (smsCodeIsTrue) {
                    findNavController().navigate(R.id.toModifyFragment, bundle)
                } else {
                    Toast.makeText(requireContext(), "验证码错误，请重新获取", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    override fun initListener() {
        super.initListener()
        binding.toLogin.setOnClickListener {
            //跳转至登录界面
            findNavController().navigate(R.id.forgetBackToLogin)
        }
        binding.registerAccount.setOnClickListener {
            //跳转至注册界面
            findNavController().navigate(R.id.forgetBackToRegister)
        }
        binding.getPhoneVerifyCode.setOnClickListener {
            //获取手机验证码
            //判空
            checkInputIsEmpty()
            if (canGetPhoneVerifyCode) {
                createTimeCount()
                //获取验证码和手机号码
                val verifyCode: String = binding.userVerifyCodeEdit.text.toString().trim()
                val userPhone: String = binding.userPhoneEdit.text.toString().trim()
                val sendSmsVo = SendSmsVo(userPhone, verifyCode)
                loginViewModel.getForgetPhoneVerifyCode(sendSmsVo)
                binding.apply {
                    if (userVerifyCodeEdit.isFocusable) {
                        userVerifyCodeEdit.clearFocus()
                    }
                    if (userPhoneEdit.isFocusable) {
                        userPhoneEdit.clearFocus()
                    }
                }
            }
        }
        binding.verifyCodeImage.setOnClickListener {
            //刷新图灵码
            if (!ClickHelper.isFastDoubleClick()) {
                loginViewModel.getCaptcha()
            }
        }
        binding.nextBtn.setOnClickListener {
            //跳转至修改密码
            if (binding.userPhoneVerifyCodeEdit.text?.isNotEmpty()!!) {
                //检查验证码是否正确
                val phoneNum: String = binding.userPhoneEdit.text.toString().trim()
                val smsCode: String = binding.userPhoneVerifyCodeEdit.text.toString().trim()
                loginViewModel.checkSmsCode(phoneNum, smsCode)
            } else {
                Toast.makeText(requireContext(), "请输入验证码", Toast.LENGTH_SHORT).show()

            }

        }
    }

    override fun onPause() {
        super.onPause()
        timeCount?.cancel()
    }

    private fun checkInputIsEmpty() {
        binding.apply {
            val verifyCodeEmpty = userVerifyCodeEdit.text?.isEmpty()!!
            val userPhoneEmpty = userPhoneEdit.text?.isEmpty()!!
            if (userPhoneEmpty && verifyCodeEmpty) {
                Toast.makeText(requireContext(), "请输入手机号及验证码", Toast.LENGTH_SHORT).show()
            } else {
                when {
                    userPhoneEmpty -> {
                        Toast.makeText(requireContext(), "请输入手机号", Toast.LENGTH_SHORT).show()
                    }
                    verifyCodeEmpty -> {
                        Toast.makeText(requireContext(), "请输入验证码", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            canGetPhoneVerifyCode = !userPhoneEmpty && !verifyCodeEmpty

        }
    }

    private fun createTimeCount() {
        timeCount = object : CountDownTimer(60000, 1000) {
            override fun onFinish() {
                if (isAdded) {
                    binding.getPhoneVerifyCode.apply {
                        isClickable = true
                        text = "重新获取验证码"
                        setTextColor(binding.root.resources.getColor(R.color.ff1296db))
                    }
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                //Log.d("myLog", "RegisterFragment().isAdded is ${RegisterFragment().isAdded}")
                if (isAdded) {
                    binding.getPhoneVerifyCode.apply {
                        isClickable = false
                        text = "(${millisUntilFinished / 1000})秒后重发"
                        setTextColor(binding.root.resources.getColor(R.color.ff666666))
                    }
                }
            }
        }.start()
    }
}