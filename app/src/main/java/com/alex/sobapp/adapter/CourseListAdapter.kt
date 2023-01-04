package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.CourseList
import com.alex.sobapp.databinding.ItemCourseBinding
import com.bumptech.glide.Glide

class CourseListAdapter : RecyclerView.Adapter<CourseListAdapter.InnerHolder>() {

    private var mStudyClickListener: OnStudyClickListener? = null
    private val courseList = arrayListOf<CourseList.Course>()

    class InnerHolder(var itemCourseBinding: ItemCourseBinding) :
        RecyclerView.ViewHolder(itemCourseBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemCourseBinding.apply {
            with(courseList[position]) {
                Glide.with(holder.itemView.context).load(cover).into(courseCover)
                courseTitle.text = title
                when (lev) {
                    "1" -> {
                        courseLevelText.text = "初级"
                    }
                    "2" -> {
                        courseLevelText.text = "中级"
                    }
                    "3" -> {
                        courseLevelText.text = "高级"
                    }
                    else -> {

                    }
                }
                courseViewCountText.text = viewCount.toInt().toString()
                when (keepUpdate) {
                    "1" -> {
                        courseStatusText.text = "连载"
                        courseStatusText.setTextColor(holder.itemView.resources.getColor(R.color.fff1851e))
                        courseStatus.setImageResource(R.mipmap.course_update)
                    }
                    else -> {
                        courseStatusText.text = "完结"
                        courseStatusText.setTextColor(holder.itemView.resources.getColor(R.color.ff1afa29))
                        courseStatus.setImageResource(R.mipmap.course_finish)
                    }
                }
                if (price.toString()=="0.0"){
                    coursePrice.text = "免费"
                    coursePrice.setTextColor(holder.itemView.resources.getColor(R.color.ff666666))
                }else{
                    coursePrice.text = "￥${price.toInt()}"
                    coursePrice.setTextColor(holder.itemView.resources.getColor(R.color.ffd81e06))
                }
                Glide.with(holder.itemView.context).load(avatar).into(teacherAvatar)
                teacherNameText.text = teacherName
                //点击开始学习
                startStudy.setOnClickListener {
                    mStudyClickListener?.onStudyClick(courseList[position].id)
                }
            }
        }
    }

    fun setOnStudyClickListener(listener:OnStudyClickListener){
        this.mStudyClickListener = listener
    }
    interface OnStudyClickListener{
        fun onStudyClick(id: String)
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    fun setData(it: MutableList<CourseList.Course>) {
        courseList.clear()
        courseList.addAll(it)
        notifyDataSetChanged()
    }
}