package com.alex.sobapp.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alex.sobapp.R
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentGuideBinding
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.utils.SizeUtils

class GuideFragment : BaseFragment<FragmentGuideBinding>() {

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentGuideBinding {
        return FragmentGuideBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()


    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.guideViewPage.apply {
            adapter = object : FragmentStateAdapter(this@GuideFragment) {
                override fun getItemCount() = 3

                override fun createFragment(position: Int) =
                    GuideDetailFragment().createGuideDetailFragment(position)
            }
            setCurrentItem(0, false)
        }
        binding.guidePointContainer.apply {
            removeAllViews()
            for (i in 0 until 3) {
                val point = View(context)
                val size: Int = SizeUtils.dp2px(requireContext(), 8f)
                val layoutParams = LinearLayout.LayoutParams(size, size)
                layoutParams.leftMargin = SizeUtils.dp2px(requireContext(), 5f)
                layoutParams.rightMargin = SizeUtils.dp2px(requireContext(), 5f)
                point.layoutParams = layoutParams
                if (i == 0) {
                    point.setBackgroundResource(R.drawable.shape_indicator_point_selected)
                } else {
                    point.setBackgroundResource(R.drawable.shape_indicator_point_normal)
                }
                addView(point)
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        binding.guideViewPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until 3) {
                    val point: View = binding.guidePointContainer.getChildAt(i)
                    if (i == position) {
                        point.setBackgroundResource(R.drawable.shape_indicator_point_selected)
                    } else {
                        point.setBackgroundResource(R.drawable.shape_indicator_point_normal)
                    }
                }
            }
        })
    }
}