package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.apiService.domain.CourseChapter
import com.alex.sobapp.databinding.ItemCourseDetailListBinding

class CourseDetailListAdapter : RecyclerView.Adapter<CourseDetailListAdapter.InnerHolder>() {

    private var mSingleItemClickListener: OnSingleItemClickListener? = null
    private val courseChapterList = arrayListOf<CourseChapter.CourseChapterItem>()

    class InnerHolder(var itemCourseDetailListBinding: ItemCourseDetailListBinding) :
        RecyclerView.ViewHolder(itemCourseDetailListBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemCourseDetailListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemCourseDetailListBinding.apply {
            val courseListItemAdapter = CourseListItemAdapter()
            courseDetailListRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = courseListItemAdapter
            }
            courseListItemAdapter.setOnItemClickListener(object :
                CourseListItemAdapter.OnItemClickListener {
                override fun onItemClick(videoId: String) {
                    mSingleItemClickListener?.onSingleItemClick(videoId)
                }

            })
            with(courseChapterList[position]) {
                courseDetailTitle.text = title
                courseDetailDesc.text = description
                courseListItemAdapter.setData(children)
            }
        }
    }

    fun setOnSingleItemClickListener(listener: OnSingleItemClickListener) {
        this.mSingleItemClickListener = listener
    }

    interface OnSingleItemClickListener {
        fun onSingleItemClick(videoId: String)
    }

    override fun getItemCount(): Int {
        return courseChapterList.size
    }

    fun setData(it: MutableList<CourseChapter.CourseChapterItem>) {
        courseChapterList.clear()
        courseChapterList.addAll(it)
        notifyDataSetChanged()
    }
}