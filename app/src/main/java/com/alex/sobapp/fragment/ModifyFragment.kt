package com.alex.sobapp.fragment

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.User
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentModifyBinding
import com.alex.sobapp.utils.ClickHelper
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.LoginViewModel

class ModifyFragment : BaseFragment<FragmentModifyBinding>() {

    private val shp = BaseApplication.getShp()
    private val sHandler = BaseApplication.getHandler()
    private var nickName: String? = ""
    private var phoneNum: String? = ""
    private var smsCode: String? = ""
    private val loginViewModel by lazy {
        LoginViewModel()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentModifyBinding {
        return FragmentModifyBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
    }

    override fun initView() {
        super.initView()
        nickName = shp?.getString("nickName", "")
        phoneNum = arguments?.getString("phoneNum", "")
        smsCode = arguments?.getString("smsCode", "")
        switchUIByState(LoadState.SUCCESS)
        if (nickName.equals("")) {
            binding.userNicknameTv.text = phoneNum
        } else {
            binding.userNicknameTv.text = nickName
        }
        binding.forgetSmsCode.text = smsCode
    }

    override fun initObserver() {
        super.initObserver()
        loginViewModel.apply {
            forgetPwdBySmsResult.observe(this@ModifyFragment, Observer {
                if (it.success) {
                    Toast.makeText(requireContext(), "密码修改成功，即将跳转至登录界面", Toast.LENGTH_SHORT).show()
                    sHandler!!.postDelayed({
                        findNavController().navigate(R.id.modifyBackToLogin)
                    }, 1500)
                } else {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun initListener() {
        super.initListener()
        binding.toLogin.setOnClickListener {
            //跳转至登录界面
            findNavController().navigate(R.id.modifyBackToLogin)
        }
        binding.registerAccount.setOnClickListener {
            //跳转至注册界面
            findNavController().navigate(R.id.modifyBackToRegister)
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
        binding.modifyPassword.setOnClickListener {
            //修改密码（通过短信）
            if (!ClickHelper.isFastDoubleClick()) {
                val password = binding.userPasswordEdit.text.toString().trim()
                val userVo = User(phoneNum!!, password)
                loginViewModel.forgetPasswordBySms(smsCode!!, userVo)
            }
        }
    }
}