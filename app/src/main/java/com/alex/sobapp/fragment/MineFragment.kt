package com.alex.sobapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.UserInfo
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentMineBinding
import com.alex.sobapp.utils.Constants.TAG
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.FocusAndFansListViewModel
import com.alex.sobapp.viewmodel.UserInfoViewModel
import com.bumptech.glide.Glide

class MineFragment : BaseFragment<FragmentMineBinding>() {

    private var isVip = false

    private val userInfoViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserInfoViewModel::class.java)
    }

    private val focusAndFansListViewModel by activityViewModels<FocusAndFansListViewModel>()

    private val shp = BaseApplication.getShp()
    private var mUserId: String? = ""
    //private var baseView: View? = null

    //override fun getPageLayoutId(): Int {
    //    return R.layout.fragment_mine
    //}

    override fun initEvent() {
        mUserId = shp!!.getString("userId", "")
    }

    override fun initView() {
        switchUIByState(LoadState.LOADING)
        //baseView = rootView
    }

    override fun initObserver() {
        userInfoViewModel.apply {
            contentValue.observe(this@MineFragment, Observer {
                loadInfo(it)
            })
            loadState.observe(this@MineFragment, Observer {
                when (it) {
                    LoadState.LOADING -> {
                        switchUIByState(LoadState.LOADING)
                    }
                    LoadState.EMPTY -> {
                        switchUIByState(LoadState.EMPTY)
                    }
                    LoadState.ERROR -> {
                        switchUIByState(LoadState.ERROR)
                    }
                    else -> {
                        switchUIByState(LoadState.SUCCESS)
                    }
                }
            })
        }.loadUserInfo(mUserId!!)
        focusAndFansListViewModel.apply {
            focusNum.observe(this@MineFragment, Observer {
                binding.userFocusNum.text = it.toString()
            })
        }.loadFocusList(mUserId!!)
        focusAndFansListViewModel.apply {
            fansNum.observe(this@MineFragment, Observer {
                binding.userFansNum.text = it.toString()
            })
        }.loadFansList(mUserId!!)
    }

    override fun initListener() {
        super.initListener()
        binding.mineDynamicPart.setOnClickListener {
            //todo:查看动态
        }
        binding.mineFocusPart.setOnClickListener {
            //查看关注列表
            val bundle = Bundle()
            bundle.putString("fromFocusOrFans", "focus")
            findNavController().navigate(R.id.toFocusListFragment, bundle)
        }
        binding.mineFansPart.setOnClickListener {
            //查看粉丝列表
            val bundle = Bundle()
            bundle.putString("fromFocusOrFans", "fans")
            findNavController().navigate(R.id.toFocusListFragment, bundle)
        }
        binding.messageNoticePart.setOnClickListener {
            //查看未读消息
            findNavController().navigate(R.id.toUnreadInfoFragment)
        }
        binding.toVip.setOnClickListener {
            //跳转至vip界面
            val bundle = Bundle()
            bundle.putBoolean("isVip", isVip)
            findNavController().navigate(R.id.toVipFragment, bundle)
        }
        binding.personalSpace.setOnClickListener {
            //我的空间
            findNavController().navigate(R.id.toPersonalSpaceFragment)
        }
    }

    private fun loadInfo(it: UserInfo) {
        binding.apply {
            Glide.with(root.context)
                .load(it.avatar)
                .placeholder(R.mipmap.default_avatar)
                .into(userAvatar)
            userName.text = it.nickname
            isVip = it.vip
            if (it.vip) {
                userName.setTextColor(resources.getColor(R.color.ffd81e06))
                userMember.text = "VIP会员"
                userMember.setTextColor(resources.getColor(R.color.ffd81e06))
                userMember.background = resources.getDrawable(R.drawable.user_member_vip_bg)
                userAvatarVipBg.visibility = View.VISIBLE
            } else {
                userName.setTextColor(resources.getColor(R.color.ff333333))
                userMember.text = "普通会员"
                userMember.setTextColor(resources.getColor(R.color.ff1afa29))
                userMember.background = resources.getDrawable(R.drawable.user_member_normal_bg)
                userAvatarVipBg.visibility = View.GONE
            }
        }

    }

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentMineBinding {
        return FragmentMineBinding.inflate(inflater, viewGroup, false)
    }


}