package com.alex.sobapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.sobapp.R
import com.alex.sobapp.activity.LoginActivity
import com.alex.sobapp.activity.MainActivity
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentGuideDetailBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import com.bumptech.glide.Glide

class GuideDetailFragment : BaseFragment<FragmentGuideDetailBinding>() {

    private var guidePosition = 0
    private val shpEdit = BaseApplication.getShpEdit()
    private var isLogin: Boolean = false

    fun createGuideDetailFragment(position: Int): GuideDetailFragment {
        val guideDetailFragment = GuideDetailFragment()
        val bundle = Bundle()
        bundle.putInt("guide_position", position + 1)
        guideDetailFragment.arguments = bundle
        return guideDetailFragment
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentGuideDetailBinding {
        return FragmentGuideDetailBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        guidePosition = arguments?.getInt("guide_position", 0)!!
        val shp = BaseApplication.getShp()
        isLogin = shp!!.getBoolean(Constants.IS_LOGIN, false)
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        when (guidePosition) {
            1 -> {
                binding.apply {
                    guideText1.text = "我们的使命"
                    guideText2.text = "让学习编程变得更加简单"
                    Glide.with(this@GuideDetailFragment).load(R.mipmap.guide_1).into(guideImage)
                    guideButton.visibility = View.GONE
                }
            }
            2 -> {
                binding.apply {
                    guideText1.text = "我们的愿景"
                    guideText2.text = "让热爱编程的年轻人成为优秀的工程师"
                    Glide.with(this@GuideDetailFragment).load(R.mipmap.guide_2).into(guideImage)
                    guideButton.visibility = View.GONE
                }
            }
            3 -> {
                binding.apply {
                    guideText1.text = "一起上班摸鱼"
                    guideText2.text = "赚钱的同时，顺便交朋友"
                    Glide.with(this@GuideDetailFragment).load(R.mipmap.guide_3).into(guideImage)
                    guideButton.visibility = View.VISIBLE
                }
            }
            else -> {

            }
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        binding.guideButton.setOnClickListener {
            shpEdit?.putBoolean(Constants.IS_FIRST_LOGIN, false)
            shpEdit?.apply()
            if (isLogin) {
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                startActivity(Intent(context, LoginActivity::class.java))
            }
            activity?.finish()
        }
    }

}