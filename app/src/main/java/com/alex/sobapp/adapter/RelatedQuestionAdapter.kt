package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.apiService.domain.RelatedQuestionList
import com.alex.sobapp.databinding.ItemRelatedQuestionBinding

class RelatedQuestionAdapter : RecyclerView.Adapter<RelatedQuestionAdapter.InnerHolder>() {

    private val relatedQuestionList = RelatedQuestionList()

    class InnerHolder(var itemRelatedQuestionBinding: ItemRelatedQuestionBinding) :
        RecyclerView.ViewHolder(itemRelatedQuestionBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemRelatedQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return relatedQuestionList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemRelatedQuestionBinding.apply {
            with(relatedQuestionList[position]) {
                relatedQuestionTitle.text = title
                userName.text = nickname
                relatedQuestionLabel.text = labels[0]
                createTimeTv.text = createTime
                viewNum.text = viewCount.toString()
                sobBNum.text = sob.toString()
            }
            relatedQuestionTitle.setOnClickListener {
                //todo:相关问题点击跳转
            }
        }
    }

    fun setData(it: RelatedQuestionList) {
        relatedQuestionList.clear()
        relatedQuestionList.addAll(it)
        notifyDataSetChanged()
    }
}