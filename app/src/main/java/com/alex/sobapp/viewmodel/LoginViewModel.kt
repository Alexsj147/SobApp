package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alex.sobapp.apiService.domain.*
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.repository.LoginRepository
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.Constants.TAG
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private val loginRepository by lazy {
        LoginRepository()
    }

    val captchaData = MutableLiveData<String>()
    val loginResponse = MutableLiveData<LoginResponse>()
    val checkSmsResult = MutableLiveData<BaseResponseResult>()
    val registerResult = MutableLiveData<BaseResponseResult>()
    val forgetPwdBySmsResult = MutableLiveData<BaseResponseResult>()

    //登录
    fun login(userName: String, password: String, verifyCode: String) {
        //println("userName is $userName")
        //println("password is $password")
        //println("verifyCode is $verifyCode")
        val shp = BaseApplication.getShp()
        val lci = shp?.getString("l_c_i", "")
        val loginBody = User(userName, password)
        viewModelScope.launch {
            try {
                val loginResult = loginRepository.login(lci!!, verifyCode, loginBody)
                println("loginResult is $loginResult")
                loginResponse.postValue(loginResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    //获取图灵验证码
    fun getCaptcha() {
        captchaData.value = Constants.BASE_URL + Constants.VERIFY_CODE_API
    }

    //获取注册的手机验证码
    fun getPhoneVerifyCode(sendSmsVo: SendSmsVo) {
        viewModelScope.launch {
            try {
                val registerPhoneVerifyCodeResult =
                    loginRepository.getRegisterPhoneVerifyCode(sendSmsVo)
                Log.d("myLog", "result is $registerPhoneVerifyCodeResult")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //获取找回密码的手机验证码（找回密码）
    fun getForgetPhoneVerifyCode(sendSmsVo: SendSmsVo) {
        viewModelScope.launch {
            try {
                val forgetPhoneVerifyCodeResult =
                    loginRepository.getForgetPhoneVerifyCode(sendSmsVo)
                Log.d("myLog", "result is $forgetPhoneVerifyCodeResult")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //检查手机验证码是否正确
    fun checkSmsCode(phoneNumber: String, smsCode: String) {
        viewModelScope.launch {
            try {
                val checkSmsCodeResult = loginRepository.checkSmsCode(phoneNumber, smsCode)
                Log.d(TAG, "checkSmsCodeResult is $checkSmsCodeResult")
                checkSmsResult.postValue(checkSmsCodeResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //注册
    fun registerAccount(smsCode: String, user: RegisterUser) {
        viewModelScope.launch {
            try {
                val registerAccountResult = loginRepository.registerAccount(smsCode, user)
                Log.d(TAG, "registerAccountResult is $registerAccountResult")
                registerResult.postValue(registerAccountResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //找回密码（通过短信找回）
    fun forgetPasswordBySms(smsCode: String, userVo: User) {
        viewModelScope.launch {
            try {
                val forgetPasswordBySmsResult = loginRepository.forgetPasswordBySms(smsCode, userVo)
                Log.d(TAG, "forgetPasswordBySmsResult is $forgetPasswordBySmsResult")
                forgetPwdBySmsResult.postValue(forgetPasswordBySmsResult)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}