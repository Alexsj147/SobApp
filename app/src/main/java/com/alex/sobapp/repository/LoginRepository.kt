package com.alex.sobapp.repository

import com.alex.sobapp.apiService.domain.RegisterUser
import com.alex.sobapp.apiService.domain.SendSmsVo
import com.alex.sobapp.apiService.domain.User
import com.alex.sobapp.utils.RetrofitClient

class LoginRepository {

    //登录
    suspend fun login(l_c_i: String, verifyCode: String, user: User) =
        RetrofitClient.apiService.login(l_c_i, verifyCode, user)

    //获取注册的手机验证码
    suspend fun getRegisterPhoneVerifyCode(sendSmsVo: SendSmsVo) =
        RetrofitClient.apiService.getPhoneVerifyCode(sendSmsVo)

    //获取找回密码的手机验证码（找回密码）
    suspend fun getForgetPhoneVerifyCode(sendSmsVo: SendSmsVo) =
        RetrofitClient.apiService.getForgetVerifyCode(sendSmsVo)

    //检查手机验证码是否正确
    suspend fun checkSmsCode(phoneNumber: String, smsCode: String) =
        RetrofitClient.apiService.checkSmsCode(phoneNumber, smsCode)

    //注册账号
    suspend fun registerAccount(smsCode: String, user: RegisterUser) =
        RetrofitClient.apiService.registerAccount(smsCode, user)

    //找回密码（通过短信找回）
    suspend fun forgetPasswordBySms(smsCode: String, userVo: User) =
        RetrofitClient.apiService.forgetPasswordBySms(smsCode, userVo)
}