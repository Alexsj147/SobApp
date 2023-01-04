package com.alex.sobapp.fragment

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.adapter.FocusAndFansAdapter
import com.alex.sobapp.apiService.domain.FocusAndFansListInfo
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentFocusAndFansBinding
import com.alex.sobapp.utils.Constants.TAG
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.utils.SizeUtils
import com.alex.sobapp.viewmodel.FocusAndFansListViewModel
import com.alex.sobapp.viewmodel.FocusViewModel

class FocusAndFansFragment : BaseFragment<FragmentFocusAndFansBinding>() {

    private val mFocusAndFansList = arrayListOf<FocusAndFansListInfo.ListItem>()
    private val focusAndFansListViewModel by activityViewModels<FocusAndFansListViewModel>()
    private val focusAndFansAdapter by lazy {
        FocusAndFansAdapter()
    }
    private val focusViewModel by lazy {
        FocusViewModel()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentFocusAndFansBinding {
        return FragmentFocusAndFansBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        mFocusAndFansList.clear()
        when (arguments?.getString("fromFocusOrFans", "")) {
            "focus" -> {
                binding.listTitle.text = "我的关注"
                val focusList = focusAndFansListViewModel.focusValue.value
                //Log.d(TAG, "focus mFocusList is $focusList ")
                mFocusAndFansList.addAll(focusList!!)
            }
            "fans" -> {
                binding.listTitle.text = "我的粉丝"
                val fansList = focusAndFansListViewModel.fansValue.value
                //Log.d(TAG, "fans mFocusList is $fansList ")
                mFocusAndFansList.addAll(fansList!!)
            }
            else -> {
            }
        }

        //Log.d(TAG, "FocusListFragment mFocusList is $mFocusList ")
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.focusOrFansList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = focusAndFansAdapter
            addItemDecoration(
                object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.apply {
                            val paddingV: Int = SizeUtils.dp2px(context, 5.0f)
                            val paddingH: Int = SizeUtils.dp2px(context, 5.0f)
                            top = paddingV
                            left = paddingH
                            right = paddingH
                            //bottom = paddingV
                        }
                    }
                }
            )
        }
        focusAndFansAdapter.setData(mFocusAndFansList)
    }

    override fun initObserver() {
        super.initObserver()
        focusViewModel.apply {
            addResult.observe(this@FocusAndFansFragment, Observer {
                Log.d(TAG, it)
            })
            deleteResult.observe(this@FocusAndFansFragment, Observer {
                Log.d(TAG, it)
            })
        }
    }

    override fun initListener() {
        super.initListener()
        binding.mineFocusAndFansBack.setOnClickListener {
            findNavController().navigateUp()
        }
        focusAndFansAdapter.setOnFocusClickListener(object :
            FocusAndFansAdapter.OnFocusClickListener {
            override fun onFocusClick(userId: String, relative: Int) {
                //todo:处理关注时的交互
                when (relative) {
                    1 -> {
                        focusViewModel.addFocus(userId)
                    }
                    2, 3 -> {
                        focusViewModel.deleteFocus(userId)
                    }
                    else -> {
                    }
                }
            }

        })
    }


}