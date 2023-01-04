package com.alex.sobapp.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentWendaBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class WendaFragment : BaseFragment<FragmentWendaBinding>() {

    //override fun getPageLayoutId(): Int {
    //    return R.layout.fragment_other
    //}

    override fun initEvent() {

    }

    override fun initView() {
        switchUIByState(LoadState.SUCCESS)
        binding.apply {
            wendaViewPager.apply {
                adapter = object : FragmentStateAdapter(this@WendaFragment) {
                    override fun getItemCount(): Int = 3

                    override fun createFragment(position: Int) =
                        WendaContentFragment().createWendaContentFragment(position)


                }
                setCurrentItem(0, false)
            }
            TabLayoutMediator(wendaTabLayout, wendaViewPager) { tab: TabLayout.Tab, position: Int ->
                tab.text = when (position) {
                    0 -> Constants.WENDA_TYPE_LASTEST
                    1 -> Constants.WENDA_TYPE_NOANSWER
                    2 -> Constants.WENDA_TYPE_HOT
                    else -> ""
                }
            }.attach()
        }
    }

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentWendaBinding {
        return FragmentWendaBinding.inflate(inflater, viewGroup, false)
    }
}