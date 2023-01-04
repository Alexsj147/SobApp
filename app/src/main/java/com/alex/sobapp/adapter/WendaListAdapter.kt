package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.WendaList
import com.alex.sobapp.databinding.ItemWendaListBinding
import com.bumptech.glide.Glide
import java.lang.StringBuilder

class WendaListAdapter : RecyclerView.Adapter<WendaListAdapter.InnerHolder>() {

    private var mQuestionTitleClickListener: OnQuestionTitleClickListener? = null
    private val wendaList = arrayListOf<WendaList.Wenda>()

    class InnerHolder(var itemWendaListBinding: ItemWendaListBinding) :
        RecyclerView.ViewHolder(itemWendaListBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemWendaListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return wendaList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemWendaListBinding.apply {
            with(wendaList[position]) {
                viewCounts.text = viewCount.toString()
                questionTitle.text = title
                answerNum.text = answerCount.toString()
                userName.text = nickname
                if (isVip == "1") {
                    userName.setTextColor(holder.itemView.resources.getColor(R.color.fff50cbb))
                } else {
                    userName.setTextColor(holder.itemView.resources.getColor(R.color.ff999999))

                }
                sobBNum.text = sob.toString()
                val labelStr = StringBuilder()
                if (labels.size == 1) {
                    wendaLabel.text = labels[0]
                } else {
                    for (element in labels) {
                        labelStr.append(element, " ")

                    }
                    wendaLabel.text = labelStr
                }
                Glide.with(holder.itemView).load(avatar).into(userAvatar)
                if (isResolve == "1") {
                    questionIsResolverPart.setBackgroundResource(R.drawable.wenda_is_resolve_bg)
                    answerNum.setTextColor(holder.itemView.resources.getColor(R.color.ff42b018))
                    answer.text = "√"
                    answer.setTextColor(holder.itemView.resources.getColor(R.color.ff42b018))
                } else {
                    questionIsResolverPart.background = null
                    answerNum.setTextColor(holder.itemView.resources.getColor(R.color.ff666666))
                    answer.text = "回答"
                    answer.setTextColor(holder.itemView.resources.getColor(R.color.ff666666))
                }
            }
            questionTitle.setOnClickListener {
                mQuestionTitleClickListener?.onQuestionTitleClick(wendaList[position].id)
            }
        }
    }

    interface OnQuestionTitleClickListener {
        fun onQuestionTitleClick(wendaId: String)
    }

    fun setOnQuestionTitleClickListener(listener: OnQuestionTitleClickListener) {
        this.mQuestionTitleClickListener = listener
    }

    fun setData(it: MutableList<WendaList.Wenda>) {
        wendaList.clear()
        wendaList.addAll(it)
        notifyDataSetChanged()
    }
}