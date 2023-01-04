package com.alex.sobapp.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.sobapp.adapter.MineInfoAdapter
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentMineInfoBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.MineInfoViewModel
import com.alex.sobapp.viewmodel.UpdateStateViewModel

class MineInfoFragment : BaseFragment<FragmentMineInfoBinding>() {

    private val mineInfoViewModel by lazy {
        MineInfoViewModel()
    }
    private val mineInfoAdapter by lazy {
        MineInfoAdapter()
    }
    private var fromMsg = ""

    private val updateStateViewModel by lazy {
        UpdateStateViewModel()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentMineInfoBinding {
        return FragmentMineInfoBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        fromMsg = arguments?.getString(Constants.FROM_MSG, "").toString()
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.mineInfoRvList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mineInfoAdapter
        }
        when (fromMsg) {
            Constants.FROM_AT_ME -> {
                binding.pageTitle.text = "@朕的"
                mineInfoViewModel.apply {
                    atMeInfoValue.observe(this@MineInfoFragment, Observer {
                        mineInfoAdapter.setAtMeInfo(it,fromMsg)
                    })
                }.loadAtMeInfo(Constants.DEFAULT_PAGE)
            }
            Constants.FROM_THUMB_UP_ME -> {
                binding.pageTitle.text = "给朕点赞"
                mineInfoViewModel.apply {
                    thumbUpListValue.observe(this@MineInfoFragment, Observer {
                        mineInfoAdapter.setThumpUpList(it,fromMsg)
                    })
                }.loadThumbUpList(Constants.DEFAULT_PAGE)
            }
            Constants.FROM_ARTICLE_COMMENT -> {
                binding.pageTitle.text = "文章评论"
                mineInfoViewModel.apply {
                    articleCommentValue.observe(this@MineInfoFragment, Observer {
                        mineInfoAdapter.setArticleComment(it, fromMsg)
                    })
                }.loadMineArticleComment(Constants.DEFAULT_PAGE)
            }
            Constants.FROM_DYNAMIC_COMMENT -> {
                binding.pageTitle.text = "动态评论"
                mineInfoViewModel.apply {
                    dynamicCommentValue.observe(this@MineInfoFragment, Observer {
                        mineInfoAdapter.setDynamicComment(it, fromMsg)
                    })
                }.loadDynamicComment(Constants.DEFAULT_PAGE)
            }
            Constants.FROM_QUESTION_ANSWER -> {
                binding.pageTitle.text = "问题回答"
                mineInfoViewModel.apply {
                    wendaListValue.observe(this@MineInfoFragment, Observer {
                        mineInfoAdapter.setWendaList(it, fromMsg)
                    })
                }.loadWendaList(Constants.DEFAULT_PAGE)
            }
            Constants.FROM_SYSTEM_INFO -> {
                binding.pageTitle.text = "系统通知"
                mineInfoViewModel.apply {
                    systemInfoValue.observe(this@MineInfoFragment, Observer {
                        mineInfoAdapter.setSystemInfo(it,fromMsg)
                    })
                }.loadSystemInfo(Constants.DEFAULT_PAGE)
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
        binding.mineInfoBack.setOnClickListener {
            findNavController().navigateUp()
        }
        mineInfoAdapter.setOnToDetailButtonClickListener(object :MineInfoAdapter.OnToDetailClickListener{
            override fun onClick(msgId: String) {
                //更新摸鱼动态状态
                //updateStateViewModel.updateDynamicState(msgId)

            }

        })
    }
}