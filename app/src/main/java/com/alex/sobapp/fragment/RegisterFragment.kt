package com.alex.sobapp.fragment

import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.RegisterUser
import com.alex.sobapp.apiService.domain.SendSmsVo
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentRegisterBinding
import com.alex.sobapp.utils.ClickHelper
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.LoginViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val sHandler = BaseApplication.getHandler()
    private val loginViewModel by lazy {
        LoginViewModel()
    }
    private var timeCount: CountDownTimer? = null
    private var canGetPhoneVerifyCode: Boolean = false
    private var smsCodeIsTrue: Boolean = false
    private var canUserRegister: Boolean = false

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        loginViewModel.getCaptcha()
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.getPhoneVerifyCode.isClickable = true

    }

    override fun initObserver() {
        super.initObserver()
        loginViewModel.apply {
            captchaData.observe(this@RegisterFragment, Observer {
                //显示图灵验证码
                Glide.with(this@RegisterFragment)
                    .load(it)//it:图灵验证码的获取地址
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//图片不需要缓存
                    .into(binding.verifyCodeImage)
            })
            checkSmsResult.observe(this@RegisterFragment, Observer {
                //判断验证码是否正确
                smsCodeIsTrue = it.success
            })
            registerResult.observe(this@RegisterFragment, Observer {
                //提示注册是否成功
                //注册成功，跳转至登录界面
                if (it.success) {
                    Toast.makeText(requireContext(), "即将跳转至登录界面", Toast.LENGTH_SHORT).show()
                    sHandler!!.postDelayed({
                        findNavController().navigate(R.id.registerBackToLogin)
                    }, 1500)
                } else {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun initListener() {
        super.initListener()
        binding.verifyCodeImage.setOnClickListener {
            //刷新图灵码
            if (!ClickHelper.isFastDoubleClick()) {
                loginViewModel.getCaptcha()
            }
        }
        binding.forgetPassword.setOnClickListener {
            //忘记密码
            findNavController().navigate(R.id.registerToForget)
        }
        binding.hasRegister.setOnClickListener {
            //跳转至登录界面
            findNavController().navigate(R.id.registerBackToLogin)
        }
        binding.userPasswordEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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
        binding.getPhoneVerifyCode.setOnClickListener {
            //获取手机验证码
            //判空
            checkInputIsEmpty()
            //Log.d("myLog", "canGetPhoneVerifyCode is $canGetPhoneVerifyCode")
            if (canGetPhoneVerifyCode) {
                createTimeCount()
                //获取验证码和手机号码
                val verifyCode: String = binding.userVerifyCodeEdit.text.toString().trim()
                val userPhone: String = binding.userPhoneEdit.text.toString().trim()
                val sendSmsVo = SendSmsVo(userPhone, verifyCode)
                loginViewModel.getPhoneVerifyCode(sendSmsVo)
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
        binding.userPhoneVerifyCodeEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.userPhoneVerifyCodeEdit.text?.isNotEmpty()!!) {
                    //检查验证码是否正确
                    val phoneNum: String = binding.userPhoneEdit.text.toString().trim()
                    val smsCode: String = binding.userPhoneVerifyCodeEdit.text.toString().trim()
                    loginViewModel.checkSmsCode(phoneNum, smsCode)
                }
            }
        }
        binding.registerBtn.setOnClickListener {
            //注册
            //判空
            checkRegisterInfo()
            if (smsCodeIsTrue) {
                if (canUserRegister) {
                    val phoneNum: String = binding.userPhoneEdit.text.toString().trim()
                    val smsCode: String = binding.userPhoneVerifyCodeEdit.text.toString().trim()
                    val userNickname: String = binding.userNicknameEdit.text.toString().trim()
                    val userPassword: String = binding.userPasswordEdit.text.toString().trim()
                    val user = RegisterUser(phoneNum, userPassword, userNickname)
                    loginViewModel.registerAccount(smsCode, user)
                }
            } else {
                Toast.makeText(requireContext(), "手机验证码错误，请重新获取", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkRegisterInfo() {
        binding.apply {
            val phoneVerifyCodeEmpty = userPhoneVerifyCodeEdit.text?.isEmpty()!!
            val userNicknameEmpty = userNicknameEdit.text?.isEmpty()!!
            val userPasswordEmpty = userPasswordEdit.text?.isEmpty()!!
            if (phoneVerifyCodeEmpty && userNicknameEmpty && userPasswordEmpty) {
                Toast.makeText(requireContext(), "请输入手机验证码，昵称和密码", Toast.LENGTH_SHORT).show()
            } else {
                when {
                    phoneVerifyCodeEmpty -> {
                        Toast.makeText(requireContext(), "请输入手机验证码", Toast.LENGTH_SHORT).show()
                    }
                    userNicknameEmpty -> {
                        Toast.makeText(requireContext(), "请输入昵称", Toast.LENGTH_SHORT).show()
                    }
                    userPasswordEmpty -> {
                        Toast.makeText(requireContext(), "请输入密码", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            canUserRegister = !phoneVerifyCodeEmpty && !userNicknameEmpty && !userPasswordEmpty
        }
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

    override fun onPause() {
        super.onPause()
        timeCount?.cancel()
    }
}