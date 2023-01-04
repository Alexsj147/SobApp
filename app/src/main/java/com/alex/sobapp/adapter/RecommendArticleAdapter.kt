package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.apiService.domain.RecommendArticle
import com.alex.sobapp.databinding.ItemRecommendArticleBinding
import com.bumptech.glide.Glide

class RecommendArticleAdapter : RecyclerView.Adapter<RecommendArticleAdapter.InnerHolder>() {

    private val recommendArticleList = arrayListOf<RecommendArticle.Data>()

    class InnerHolder(var itemRecommendArticleBinding: ItemRecommendArticleBinding) :
        RecyclerView.ViewHolder(itemRecommendArticleBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemRecommendArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return recommendArticleList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemRecommendArticleBinding.apply {
            val root = this.root
            with(recommendArticleList[position]) {
                Glide.with(root.context).load(covers[0]).into(recommendArticleCover)
                recommendArticleTitle.text = title
                recommendArticleUserName.text = nickname
                recommendArticleCreateTime.text = createTime
                recommendArticleViews.text = viewCount
                recommendArticleThumbUp.text = thumbUp.toString()
                val articleLabel = StringBuilder()
                for (label in labels) {
                    articleLabel.append(" / $label")
                }
                //for (i in labels.indices) {
                //    str = str.concat(" ").concat(list.get(i))
                //}
                recommendArticleLable.text = articleLabel
            }
        }
    }

    fun setData(it: MutableList<RecommendArticle.Data>) {
        recommendArticleList.clear()
        recommendArticleList.addAll(it)
        notifyDataSetChanged()
    }
}