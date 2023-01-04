package com.alex.sobapp.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentPhotoBinding
import com.alex.sobapp.utils.LoadState
import com.bumptech.glide.Glide
import java.lang.Exception

class PhotoFragment : BaseFragment<FragmentPhotoBinding>() {

    private var photoPath: String? = ""

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentPhotoBinding {
        return FragmentPhotoBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        photoPath = arguments?.getString("photoPath", "")
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        Glide.with(binding.root.context).load(photoPath).into(binding.photoView)
    }

    override fun initListener() {
        super.initListener()
        binding.photoCancel.setOnClickListener {
            try {
                findNavController().navigateUp()
            } catch (e: Exception) {
                Log.d("myLog", "exception is $e")
            }

        }
    }

}