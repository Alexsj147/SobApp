package com.alex.sobapp.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.adapter.CourseListAdapter
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentSchoolBinding
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.utils.SizeUtils
import com.alex.sobapp.viewmodel.SchoolViewModel

class SchoolFragment : BaseFragment<FragmentSchoolBinding>() {


    private val schoolViewModel by lazy {
        SchoolViewModel()
    }
    private val courseListAdapter by lazy {
        CourseListAdapter()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentSchoolBinding {
        return FragmentSchoolBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.courseListRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = courseListAdapter
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
                        //bottom = paddingV
                    }
                }
            })
        }
        schoolViewModel.loadCourseList()
    }

    override fun initObserver() {
        super.initObserver()
        schoolViewModel.apply {
            courseListValue.observe(this@SchoolFragment, Observer {
                courseListAdapter.setData(it)
            })
            loadState.observe(this@SchoolFragment, Observer {

            })
        }
    }

    override fun initListener() {
        super.initListener()
        courseListAdapter.setOnStudyClickListener(object : CourseListAdapter.OnStudyClickListener {
            override fun onStudyClick(id: String) {
                //跳转至课程详情界面
                val bundle = Bundle()
                bundle.putString("courseId", id)
                findNavController().navigate(R.id.toCourseDetailFragment, bundle)
            }

        })
    }
}