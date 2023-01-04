package com.alex.sobapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.sobapp.R
import com.alex.sobapp.adapter.WendaListAdapter
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentWendaContentBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.WendaListViewModel
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.ClassicsHeader

class WendaContentFragment : BaseFragment<FragmentWendaContentBinding>() {

    private var wendaPosition = 0
    private val wendaListAdapter by lazy {
        WendaListAdapter()
    }
    private val wendaListViewModel by lazy {
        WendaListViewModel()
    }

    fun createWendaContentFragment(position: Int): WendaContentFragment {
        val wendaContentFragment = WendaContentFragment()
        val bundle = Bundle()
        bundle.putInt(Constants.WENDA_TYPE, position + 1)
        wendaContentFragment.arguments = bundle
        return wendaContentFragment
    }

    override fun initEvent() {
        super.initEvent()
        wendaPosition = arguments?.getInt(Constants.WENDA_TYPE, 0)!!
        when (wendaPosition) {
            1 -> {
                //binding.textView3.text = "最新提问"
                wendaListViewModel.loadWendaList(Constants.TYPE_LASTEST, Constants.WENDA_CATEGORY)

            }
            2 -> {
                //binding.textView3.text = "等待解答"
                wendaListViewModel.loadWendaList(
                    Constants.TYPE_NOANSWER,
                    Constants.WENDA_CATEGORY
                )
            }
            3 -> {
                //binding.textView3.text = "热门推荐"
                wendaListViewModel.loadWendaList(Constants.TYPE_HOT, Constants.WENDA_CATEGORY)
            }
            else -> {
            }
        }
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.wendaRefreshLayout.apply {
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(BallPulseFooter(context))
        }
        binding.wendaListRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wendaListAdapter
        }

    }

    override fun onResume() {
        super.onResume()

    }

    override fun initObserver() {
        super.initObserver()
        wendaListViewModel.apply {
            wendaListValue.observe(this@WendaContentFragment, Observer {
                wendaListAdapter.setData(it)
            })
            loadState.observe(this@WendaContentFragment, Observer {
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
                        binding.wendaRefreshLayout.finishRefresh()
                    }
                }
                when (it) {
                    LoadState.LOADING_MORE_SUCCESS -> {
                        binding.wendaRefreshLayout.finishLoadMore()
                    }
                    LoadState.LOADING_MORE_EMPTY -> {
                        Toast.makeText(context, "已经加载了所有的内容", Toast.LENGTH_SHORT).show()
                    }
                    LoadState.LOADING_MORE_ERROR -> {
                        Toast.makeText(context, "网络不佳，请稍后重试", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }

                }
            })
        }
    }

    override fun initListener() {
        super.initListener()
        wendaListAdapter.setOnQuestionTitleClickListener(object :
            WendaListAdapter.OnQuestionTitleClickListener {
            override fun onQuestionTitleClick(wendaId: String) {
                //跳转至问答详情
                val bundle = Bundle()
                bundle.putString(Constants.WENDAID, wendaId)
                findNavController().navigate(R.id.toWendaDetailFragment, bundle)
            }

        })
        binding.wendaRefreshLayout.setOnRefreshListener {
            //todo:刷新
            Log.d("myLog", "触发了刷新")
            //binding.wendaRefreshLayout.finishRefresh(2000)
            when (wendaPosition) {
                1 -> {
                    wendaListViewModel.loadWendaList(
                        Constants.TYPE_LASTEST,
                        Constants.WENDA_CATEGORY
                    )
                }
                2 -> {
                    wendaListViewModel.loadWendaList(
                        Constants.TYPE_NOANSWER,
                        Constants.WENDA_CATEGORY
                    )
                }
                3 -> {
                    wendaListViewModel.loadWendaList(Constants.TYPE_HOT, Constants.WENDA_CATEGORY)
                }
                else -> {
                }
            }

        }
        binding.wendaRefreshLayout.setOnLoadMoreListener {
            //todo:加载更多
            Log.d("myLog", "触发了加载更多")
            binding.wendaRefreshLayout.finishLoadMore(2000)
            when (wendaPosition) {
                1 -> {
                    wendaListViewModel.loadMoreWendaList(
                        Constants.TYPE_LASTEST,
                        Constants.WENDA_CATEGORY
                    )

                }
                2 -> {
                    wendaListViewModel.loadMoreWendaList(
                        Constants.TYPE_NOANSWER,
                        Constants.WENDA_CATEGORY
                    )
                }
                3 -> {
                    wendaListViewModel.loadMoreWendaList(
                        Constants.TYPE_HOT,
                        Constants.WENDA_CATEGORY
                    )
                }
                else -> {
                }
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentWendaContentBinding {
        return FragmentWendaContentBinding.inflate(inflater, viewGroup, false)
    }
}