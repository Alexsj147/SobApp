package com.alex.sobapp.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentVipBinding
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.VipViewModel

class VipFragment : BaseFragment<FragmentVipBinding>() {

    private var isVip = false
    private val vipViewModel by lazy {
        VipViewModel()
    }

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentVipBinding {
        return FragmentVipBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        arguments?.let {
            isVip = it.getBoolean("isVip", false)
        }
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        //根据用户的vip状态，调整显示内容：升级成为VIP，领取津贴，已领取津贴
        binding.apply {
            when (isVip) {
                true -> {
                    vipPowerBtn.text = "领取津贴"
                }
                else -> {
                    vipPowerBtn.text = "升级成为VIP"
                }
            }
        }
        if (isVip) {
            vipViewModel.checkVipAllowance()
        }

    }

    override fun initObserver() {
        super.initObserver()
        vipViewModel.apply {
            vipCheckResult.observe(this@VipFragment, Observer {
                //Log.d("myLog", "vipCheckResult is $it")
                binding.apply {
                    if (!it.success) {
                        vipPowerBtn.text = "领取津贴"
                        vipPowerBtn.setTextColor(resources.getColor(R.color.white))
                        vipPowerBtn.background = resources.getDrawable(R.drawable.become_vip_bg)
                    } else {
                        vipPowerBtn.text = "已领取津贴"
                        vipPowerBtn.setTextColor(resources.getColor(R.color.ff333333))
                        vipPowerBtn.background = resources.getDrawable(R.drawable.has_became_vip_bg)
                    }
                }

            })
            vipGetResult.observe(this@VipFragment, Observer {
                //Log.d("myLog", "vipGetResult is $it")
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                vipViewModel.checkVipAllowance()
            })
        }
    }

    override fun initListener() {
        super.initListener()
        binding.vipBack.setOnClickListener {
            //返回
            findNavController().navigateUp()
        }
        binding.vipPowerBtn.setOnClickListener {
            if (isVip) {
                //vip领取津贴
                vipViewModel.getVipAllowance()
            } else {
                //todo:跳转至vip购买界面
            }
        }
    }
}