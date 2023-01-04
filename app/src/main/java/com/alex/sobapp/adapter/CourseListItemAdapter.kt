package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.apiService.domain.CourseChapter
import com.alex.sobapp.databinding.ItemCourseListItemBinding

class CourseListItemAdapter : RecyclerView.Adapter<CourseListItemAdapter.InnerHolder>() {

    private var mItemClickListener: OnItemClickListener? = null
    private val courseChildrenList = arrayListOf<CourseChapter.CourseChapterItem.Children>()

    class InnerHolder(var itemCourseListItemBinding: ItemCourseListItemBinding) :
        RecyclerView.ViewHolder(itemCourseListItemBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemCourseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemCourseListItemBinding.apply {
            with(courseChildrenList[position]) {
                singleCourseItemTitle.text = title
            }
            holder.itemView.setOnClickListener {
                mItemClickListener?.onItemClick(courseChildrenList[position].id)
            }
        }
    }

    fun setOnItemClickListener(listener:OnItemClickListener){
        this.mItemClickListener = listener
    }
    interface OnItemClickListener{
        fun onItemClick(videoId: String)
    }

    override fun getItemCount(): Int {
        return courseChildrenList.size
    }

    fun setData(children: List<CourseChapter.CourseChapterItem.Children>) {
        courseChildrenList.clear()
        courseChildrenList.addAll(children)
        notifyDataSetChanged()
    }
}