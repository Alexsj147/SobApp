package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.apiService.domain.TopicList
import com.alex.sobapp.databinding.ItemTopicBinding
import com.bumptech.glide.Glide

class TopicAdapter : RecyclerView.Adapter<TopicAdapter.InnerHolder>() {

    private var mItemClickListener: OnItemClickListener? = null
    private val topicList = arrayListOf<TopicList.Data>()

    class InnerHolder(var itemTopicBinding: ItemTopicBinding) :
        RecyclerView.ViewHolder(itemTopicBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(topicId: String)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemTopicBinding.apply {
            with(topicList[position]) {
                Glide.with(holder.itemView).load(cover).into(topicCover)
                topicTitle.text = topicName
                topicDesc.text = description
            }
        }
        holder.itemView.setOnClickListener {
            val id = topicList[position].id
            mItemClickListener?.onItemClick(id)
        }
    }

    fun setData(list: MutableList<TopicList.Data>) {
        topicList.clear()
        topicList.addAll(list)
        notifyDataSetChanged()
    }
}