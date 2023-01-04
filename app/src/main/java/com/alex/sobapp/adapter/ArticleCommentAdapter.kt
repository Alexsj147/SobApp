package com.alex.sobapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.ArticleComments
import com.alex.sobapp.databinding.ItemArticleCommentsBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_comments.view.*

class ArticleCommentAdapter : RecyclerView.Adapter<ArticleCommentAdapter.InnerHolder>() {


    private val articleCommentsList = arrayListOf<ArticleComments.Content>()

    class InnerHolder(var itemArticleCommentsBinding: ItemArticleCommentsBinding) :
        RecyclerView.ViewHolder(itemArticleCommentsBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemArticleCommentsBinding =
            ItemArticleCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemArticleCommentsBinding)
    }

    override fun getItemCount(): Int {
        return articleCommentsList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemArticleCommentsBinding.apply {
            val root = this.root
            //设置楼中楼
            val articleSubCommentAdapter = ArticleSubCommentAdapter()
            articleSubCommentRv.run {
                layoutManager = LinearLayoutManager(root.context)
                adapter = articleSubCommentAdapter
            }
            //加载评论数据
            with(articleCommentsList[position]) {
                Glide.with(root.context).load(this.avatar).into(userAvatar)
                userName.text = this.nickname
                articleComment.text = this.commentContent
                createTime.text = this.publishTime
                if (this.vip) {
                    userName.setTextColor(root.resources.getColor(R.color.fff50cbb))
                }
                if (this.subComments.isEmpty()) {

                }else{
                    articleSubCommentAdapter.setData(subComments)
                }
            }
        }
    }

    fun setData(it: MutableList<ArticleComments.Content>) {
        articleCommentsList.clear()
        articleCommentsList.addAll(it)
        notifyDataSetChanged()
    }
}