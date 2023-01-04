package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.ArticleComments
import com.alex.sobapp.databinding.ItemArticleSubCommentBinding
import com.bumptech.glide.Glide

class ArticleSubCommentAdapter : RecyclerView.Adapter<ArticleSubCommentAdapter.InnerHolder>() {

    private val subCommentList = arrayListOf<ArticleComments.Content.SubComment>()

    class InnerHolder(var itemArticleSubCommentBinding: ItemArticleSubCommentBinding) :
        RecyclerView.ViewHolder(itemArticleSubCommentBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemArticleSubCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return subCommentList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemArticleSubCommentBinding.apply {
            val root = this.root
            with(subCommentList[position]) {
                Glide.with(root.context).load(yourAvatar).into(userAvatar)
                userName.text = yourNickname
                targetUserNickNameTv.text = "@$beNickname"
                articleSubCommentTv.text = content
                createTimeTv.text = publishTime
                if (vip) {
                    userName.setTextColor(root.resources.getColor(R.color.fff50cbb))
                }
            }
        }
    }

    fun setData(subComments: List<ArticleComments.Content.SubComment>) {
        subCommentList.clear()
        subCommentList.addAll(subComments)
        notifyDataSetChanged()
    }
}