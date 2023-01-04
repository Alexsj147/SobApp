package com.alex.sobapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentPersonalSpaceContentBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState

class PersonalSpaceContentFragment : BaseFragment<FragmentPersonalSpaceContentBinding>() {

    private var personalSpacePosition = 0

    fun createPersonalSpaceContentFragment(position: Int): PersonalSpaceContentFragment {
        val personalSpaceContentFragment = PersonalSpaceContentFragment()
        val bundle = Bundle()
        bundle.putInt(Constants.PERSONAL_SPACE_TYPE, position + 1)
        personalSpaceContentFragment.arguments = bundle
        return personalSpaceContentFragment
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentPersonalSpaceContentBinding {
        return FragmentPersonalSpaceContentBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        switchUIByState(LoadState.SUCCESS)
        personalSpacePosition = arguments?.getInt(Constants.PERSONAL_SPACE_TYPE, 0)!!
        when (personalSpacePosition) {
            1 -> {
                binding.textView23.text = Constants.PERSONAL_SPACE_TYPE_MOYU


            }
            2 -> {
                binding.textView23.text = Constants.PERSONAL_SPACE_TYPE_ARTICLE

            }
            3 -> {
                binding.textView23.text = Constants.PERSONAL_SPACE_TYPE_QUESTION

            }
            else -> {
            }
        }
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
    }
}