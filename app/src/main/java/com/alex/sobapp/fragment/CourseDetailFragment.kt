package com.alex.sobapp.fragment

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.activity.VideoPlayActivity
import com.alex.sobapp.adapter.CourseDetailListAdapter
import com.alex.sobapp.adapter.CourseListAdapter
import com.alex.sobapp.apiService.domain.CourseDetail
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentCourseDetailBinding
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.utils.SizeUtils
import com.alex.sobapp.viewmodel.CourseViewModel
import com.bumptech.glide.Glide

class CourseDetailFragment : BaseFragment<FragmentCourseDetailBinding>() {

    private val courseViewModel by lazy {
        CourseViewModel()
    }
    private val courseDetailListAdapter by lazy {
        CourseDetailListAdapter()
    }

    private var courseId: String = ""

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentCourseDetailBinding {
        return FragmentCourseDetailBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        arguments?.let {
            courseId = it.getString("courseId", "")
        }
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        courseViewModel.loadCourseDetail(courseId)
        courseViewModel.loadCourseChapter(courseId)
        binding.courseChapterListRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = courseDetailListAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.apply {
                        val paddingV: Int = SizeUtils.dp2px(context, 10.0f)
                        val paddingH: Int = SizeUtils.dp2px(context, 10.0f)
                        //top = paddingV
                        left = paddingH
                        right = paddingH
                        bottom = paddingV
                    }
                }
            })
        }
    }

    override fun initObserver() {
        super.initObserver()
        courseViewModel.apply {
            courseDetailValue.observe(this@CourseDetailFragment, Observer {
                loadInfo(it)
            })
            courseChapterValue.observe(this@CourseDetailFragment, Observer {
                courseDetailListAdapter.setData(it)
            })
        }

    }

    private fun loadInfo(courseDetail: CourseDetail) {
        binding.apply {
            with(courseDetail) {
                Glide.with(this@CourseDetailFragment).load(cover).into(courseCover)
                courseTitle.text = title
                Glide.with(this@CourseDetailFragment).load(avatar).into(teacherAvatar)
                teacherNameText.text = teacherName
                when (lev) {
                    1.0 -> {
                        courseLevelText.text = "初级"
                    }
                    2.0 -> {
                        courseLevelText.text = "中级"
                    }
                    else -> {
                        courseLevelText.text = "高级"
                    }
                }
                courseViewCountText.text = viewCount.toInt().toString()
                if (price == 0.0) {
                    coursePrice.text = "免费课程"
                    isVipFreeText.visibility = View.GONE
                } else {
                    coursePrice.text = "￥${price.toInt()}"
                    isVipFreeText.visibility = View.VISIBLE
                }
                if (isVipFree == "1") {
                    isVipFreeText.text = "VIP免费"
                }
                courseStudyNumText.text = buyCount.toInt().toString()
                //Log.d("myLog", "课程状态status is $status")
            }

        }
    }

    override fun initListener() {
        super.initListener()
        binding.courseDetailBack.setOnClickListener {
            findNavController().navigateUp()
        }
        courseDetailListAdapter.setOnSingleItemClickListener(object :
            CourseDetailListAdapter.OnSingleItemClickListener {
            override fun onSingleItemClick(videoId: String) {
                Log.d("myLog", "videoId is $videoId")
                val intent = Intent(requireContext(), VideoPlayActivity::class.java)
                intent.putExtra("videoId", videoId)
                startActivity(intent)
            }

        })
    }

}