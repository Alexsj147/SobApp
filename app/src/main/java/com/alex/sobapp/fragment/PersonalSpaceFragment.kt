package com.alex.sobapp.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.UserInfo
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentPersonalSpaceBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.UserInfoViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PersonalSpaceFragment : BaseFragment<FragmentPersonalSpaceBinding>() {

    private val userInfoViewModel by lazy {
        UserInfoViewModel()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentPersonalSpaceBinding {
        return FragmentPersonalSpaceBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        val shp = BaseApplication.getShp()
        val userId = shp?.getString("userId", "")
        userInfoViewModel.loadUserInfo(userId!!)
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.apply {
            personalSpaceViewPager.apply {
                adapter = object : FragmentStateAdapter(this@PersonalSpaceFragment) {
                    override fun getItemCount(): Int = 3

                    override fun createFragment(position: Int): Fragment =
                        PersonalSpaceContentFragment().createPersonalSpaceContentFragment(position)
                }
                setCurrentItem(0, false)
            }
            TabLayoutMediator(
                personalSpaceTab,
                personalSpaceViewPager
            ) { tab: TabLayout.Tab, position: Int ->
                tab.text = when (position) {
                    0 -> Constants.PERSONAL_SPACE_TYPE_MOYU
                    1 -> Constants.PERSONAL_SPACE_TYPE_ARTICLE
                    2 -> Constants.PERSONAL_SPACE_TYPE_QUESTION
                    else -> ""
                }
            }.attach()
        }
    }

    override fun initObserver() {
        super.initObserver()
        userInfoViewModel.apply {
            contentValue.observe(this@PersonalSpaceFragment, Observer {
                loadInfo(it)
            })
        }
    }

    private fun loadInfo(it: UserInfo) {
        binding.apply {
            Glide.with(this@PersonalSpaceFragment).load(it.avatar).into(userAvatar)
            userName.text = it.nickname
            if (it.sign==null) {
                userSign.text ="这个人很懒，什么都没有写"
            }else{
                userSign.text = it.sign
            }
            userUid.text = "uid:${it.userId}"
            if (it.vip) {
                userName.setTextColor(resources.getColor(R.color.ffd81e06))
                userAvatarVipBg.visibility = View.VISIBLE
            } else {
                userName.setTextColor(resources.getColor(R.color.ff333333))
                userAvatarVipBg.visibility = View.GONE
            }
        }
    }

    override fun initListener() {
        super.initListener()
    }
}