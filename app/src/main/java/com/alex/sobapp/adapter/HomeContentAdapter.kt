package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.RecommendList
import com.alex.sobapp.databinding.ItemHomeContentBinding
import com.bumptech.glide.Glide

class HomeContentAdapter : RecyclerView.Adapter<HomeContentAdapter.InnerHolder>() {

    private var itemClickListener: OnItemClickListener? = null
    private val recommendList = arrayListOf<RecommendList.Info>()


    class InnerHolder(var itemHomeContentBinding: ItemHomeContentBinding) :
        RecyclerView.ViewHolder(itemHomeContentBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        //val itemView =
        //    LayoutInflater.from(parent.context).inflate(R.layout.item_home_content, parent, false)
        //return InnerHolder(itemView)
        val itemBinding =
            ItemHomeContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return recommendList.size

    }

    interface OnItemClickListener {
        fun onClick(id: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemHomeContentBinding.apply {
            val root = this.root
            with(recommendList[position]) {
                Glide.with(root.context)
                    .load(covers[0])
                    .placeholder(R.mipmap.default_img)
                    .into(contentCover)
                contentTitle.text = title
                Glide.with(root.context)
                    .load(avatar)
                    .placeholder(R.mipmap.default_avatar)
                    .into(userAvatar)
                nickname.text = nickName
                createTimeTv.text = createTime
                views.text = viewCount.toString()
                thumup.text = thumbUp.toString()
                if (vip) {
                    nickname.setTextColor(root.resources.getColor(R.color.fff50cbb))
                }
            }
        }
        holder.itemHomeContentBinding.root.setOnClickListener {
            itemClickListener?.onClick(recommendList[position].id)
        }
    }


    fun setData(it: MutableList<RecommendList.Info>) {
        recommendList.clear()
        recommendList.addAll(it)
        notifyDataSetChanged()
    }
}