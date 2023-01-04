package com.alex.sobapp.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.activity.LoginActivity
import com.alex.sobapp.activity.MainActivity
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentWelcomeBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    private var isLogin: Boolean = false
    private var isFirstLogin: Boolean = true

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentWelcomeBinding {
        return FragmentWelcomeBinding.inflate(inflater, viewGroup, false)
    }

    private fun guide() {

        val handler = BaseApplication.getHandler()
        handler!!.postDelayed({
            if (isFirstLogin) {
                findNavController().navigate(R.id.toGuideFragment)
            } else {

                if (isLogin) {
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()

                } else {
                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()

                }
            }
        }, 2000)
    }

    override fun initEvent() {
        super.initEvent()
        val shp = BaseApplication.getShp()
        isLogin = shp!!.getBoolean(Constants.IS_LOGIN, false)
        isFirstLogin = shp.getBoolean(Constants.IS_FIRST_LOGIN, false)
        Log.d("myLog", "isFirstLogin is $isFirstLogin")
        //isFirstLogin==true

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        checkPermission()
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        val readExternalPermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val writeExternalPermission =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        if (readExternalPermission == PackageManager.PERMISSION_GRANTED
            && writeExternalPermission == PackageManager.PERMISSION_GRANTED
            && cameraPermission == PackageManager.PERMISSION_GRANTED
        ) {
            //有权限
            guide()
        } else {
            //无权限，进行申请
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                Constants.PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PERMISSION_REQUEST_CODE) {
            guide()
        }
    }
}