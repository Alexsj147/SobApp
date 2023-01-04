package com.alex.sobapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.UnreadInfo
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.custom.OneStepReadDialog
import com.alex.sobapp.databinding.FragmentUnreadInfoBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.UnreadInfoViewModel


class UnreadInfoFragment : BaseFragment<FragmentUnreadInfoBinding>() {

    private val unreadInfoViewModel by lazy {
        UnreadInfoViewModel()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentUnreadInfoBinding {
        return FragmentUnreadInfoBinding.inflate(inflater, viewGroup, false)
    }

    override fun onResume() {
        super.onResume()
        unreadInfoViewModel.getUnreadInfo()
    }

    override fun initEvent() {
        super.initEvent()
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.atMeTitle.text = "@朕消息"
    }

    override fun initObserver() {
        super.initObserver()
        unreadInfoViewModel.apply {
            unreadInfoValue.observe(this@UnreadInfoFragment, Observer {
                loadUnreadInfo(it)
            })
            loadState.observe(this@UnreadInfoFragment, Observer {
                when (it) {
                    LoadState.SUCCESS -> {
                        switchUIByState(LoadState.SUCCESS)
                    }
                    LoadState.ERROR -> {
                        switchUIByState(LoadState.ERROR)
                    }
                }
            })
            oneStepReadValue.observe(this@UnreadInfoFragment, Observer {
                //if (it.success) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                //}
            })
        }
    }

    private fun loadUnreadInfo(it: UnreadInfo) {
        binding.replyToMeNum.apply {
            text = it.atMsgCount.toInt().toString()
            if (it.atMsgCount == 0) {
                visibility = View.INVISIBLE
            }
        }
        binding.thumbUpMeNum.apply {
            text = it.thumbUpMsgCount.toInt().toString()
            if (it.thumbUpMsgCount == 0) {
                visibility = View.INVISIBLE
            }
        }
        binding.articleCommentNum.apply {
            text = it.articleMsgCount.toInt().toString()
            if (it.articleMsgCount == 0) {
                visibility = View.INVISIBLE
            }
        }
        binding.dynamicCommentNum.apply {
            text = it.momentCommentCount.toInt().toString()
            if (it.momentCommentCount == 0) {
                visibility = View.INVISIBLE
            }
        }
        binding.questionAnswerNum.apply {
            text = it.wendaMsgCount.toInt().toString()
            if (it.wendaMsgCount == 0) {
                visibility = View.INVISIBLE
            }
        }
        binding.systemInfoNum.apply {
            text = it.systemMsgCount.toInt().toString()
            if (it.systemMsgCount == 0) {
                visibility = View.INVISIBLE
            }
        }
    }

    override fun initListener() {
        super.initListener()
        val bundle = Bundle()
        binding.unreadInfoBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.replyToMePart.setOnClickListener {
            //回复我的
            bundle.putString(Constants.FROM_MSG, Constants.FROM_AT_ME)
            findNavController().navigate(R.id.toMineInfoFragment, bundle)
        }
        binding.thumbUpMePart.setOnClickListener {
            //给朕点赞
            bundle.putString(Constants.FROM_MSG, Constants.FROM_THUMB_UP_ME)
            findNavController().navigate(R.id.toMineInfoFragment, bundle)
        }
        binding.articleCommentPart.setOnClickListener {
            //文章评论
            bundle.putString(Constants.FROM_MSG, Constants.FROM_ARTICLE_COMMENT)
            findNavController().navigate(R.id.toMineInfoFragment, bundle)
        }
        binding.dynamicCommentPart.setOnClickListener {
            //动态评论
            bundle.putString(Constants.FROM_MSG, Constants.FROM_DYNAMIC_COMMENT)
            findNavController().navigate(R.id.toMineInfoFragment, bundle)
        }
        binding.questionAnswerPart.setOnClickListener {
            //问题回答
            bundle.putString(Constants.FROM_MSG, Constants.FROM_QUESTION_ANSWER)
            findNavController().navigate(R.id.toMineInfoFragment, bundle)
        }
        binding.systemInfoPart.setOnClickListener {
            //系统通知
            bundle.putString(Constants.FROM_MSG, Constants.FROM_SYSTEM_INFO)
            findNavController().navigate(R.id.toMineInfoFragment, bundle)
        }
        binding.oneStepRead.setOnClickListener {
            //全部已读
            createDialog(it)
        }
    }

    private fun createDialog(it: View) {
        val dialog = OneStepReadDialog(it.context)
        //Log.d("myLog","dialog is $dialog")
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnCancelClickListener(object : OneStepReadDialog.OnCancelClickListener {
            override fun onCancelClick() {
                //取消
                dialog.dismiss()
            }
        })
        dialog.setOnConfirmClickListener(object : OneStepReadDialog.OnConfirmClickListener {
            override fun onConfirmClick() {
                //确认
                //一键已读
                unreadInfoViewModel.oneStepRead()
                dialog.dismiss()
            }
        })
        dialog.show()
    }


}