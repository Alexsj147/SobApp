package com.alex.sobapp.fragment

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.adapter.RelatedQuestionAdapter
import com.alex.sobapp.adapter.WendaAnswerListAdapter
import com.alex.sobapp.apiService.domain.WendaDetail
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentWendaDetailBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.utils.SizeUtils
import com.alex.sobapp.viewmodel.WendaDetailViewModel
import com.bumptech.glide.Glide

class WendaDetailFragment : BaseFragment<FragmentWendaDetailBinding>() {

    private var wendaId = ""
    private val wendaDetailViewModel by lazy {
        WendaDetailViewModel()
    }
    private val wendaAnswerListAdapter by lazy {
        WendaAnswerListAdapter()
    }
    private val relatedQuestionAdapter by lazy {
        RelatedQuestionAdapter()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentWendaDetailBinding {
        return FragmentWendaDetailBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        wendaId = arguments?.getString(Constants.WENDAID, "").toString()
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.apply {
            questionContentWeb.settings.useWideViewPort = true
            questionContentWeb.settings.loadWithOverviewMode = true
            questionContentWeb.settings.textZoom = 100
            questionContentWeb.settings.javaScriptEnabled = true
            wendaAnswerListRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = wendaAnswerListAdapter
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.apply {
                            val paddingV: Int = SizeUtils.dp2px(context, 5.0f)
                            val paddingH: Int = SizeUtils.dp2px(context, 1.0f)
                            top = paddingV
                            //left = paddingH
                            //right = paddingH
                            //bottom = paddingV
                        }
                    }
                })
            }
            relatedQuestionListRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = relatedQuestionAdapter
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        wendaDetailViewModel.apply {
            wendaDetailValue.observe(this@WendaDetailFragment, Observer {
                loadDetail(it)
            })
            loadState.observe(this@WendaDetailFragment, Observer {

            })
        }.loadWendaDetail(wendaId)
        wendaDetailViewModel.apply {
            wendaAnswerValue.observe(this@WendaDetailFragment, Observer {
                wendaAnswerListAdapter.setData(it)
            })
        }.loadWendaAnswer(wendaId)
        wendaDetailViewModel.apply {
            relatedQuestionValue.observe(this@WendaDetailFragment, Observer {
                relatedQuestionAdapter.setData(it)
            })
        }.loadRelatedQuestion(wendaId)
    }

    private fun loadDetail(it: WendaDetail) {
        binding.apply {
            Glide.with(this@WendaDetailFragment).load(it.avatar).into(userAvatar)
            userNickName.text = it.nickname
            if (it.isVip == "1") {
                userNickName.setTextColor(this@WendaDetailFragment.resources.getColor(R.color.fff50cbb))
            } else {
                userNickName.setTextColor(this@WendaDetailFragment.resources.getColor(R.color.ff666666))
            }
            createTimeTv.text = it.createTime
            viewsNum.text = it.viewCount.toString()
            sobBNum.text = it.sob.toString()
            questionContentWeb.loadUrl("file:///android_asset/vue2/index.html?id=$wendaId")
        }


    }

    override fun initListener() {
        super.initListener()
        binding.wendaBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}