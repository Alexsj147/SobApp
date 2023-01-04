package com.alex.sobapp.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.WendaAnswer
import com.alex.sobapp.databinding.ItemWendaSubCommentBinding
import com.bumptech.glide.Glide

class WendaSubCommentAdapter : RecyclerView.Adapter<WendaSubCommentAdapter.InnerHolder>() {

    private var wendaSubCommentList = arrayListOf<WendaAnswer.Data.WendaSubComment>()

    class InnerHolder(var itemWendaSubCommentBinding: ItemWendaSubCommentBinding) :
        RecyclerView.ViewHolder(itemWendaSubCommentBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemWendaSubCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return wendaSubCommentList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemWendaSubCommentBinding.apply {
            with(wendaSubCommentList[position]) {
                Glide.with(holder.itemView).load(yourAvatar).into(userAvatar)
                userName.text = yourNickname
                if (vip){
                    userName.setTextColor(holder.itemView.resources.getColor(R.color.fff50cbb))
                }else{
                    userName.setTextColor(holder.itemView.resources.getColor(R.color.ff999999))
                }
                val spannableString = SpannableString("回复@$beNickname")
                val colorSpan = ForegroundColorSpan(root.resources.getColor(R.color.ff1296db))
                spannableString.setSpan(
                    colorSpan,
                    2,
                    spannableString.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                targetName.text = spannableString
                wendaSubCommentContent.text = content
                createTime.text = publishTime
            }
            responseButton.setOnClickListener {
                //todo:问答sub回复
            }
        }
    }

    fun setData(it: List<WendaAnswer.Data.WendaSubComment>) {
        wendaSubCommentList.clear()
        wendaSubCommentList.addAll(it)
        notifyDataSetChanged()
    }

}