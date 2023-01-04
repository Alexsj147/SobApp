package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.WendaAnswer
import com.alex.sobapp.databinding.ItemWendaAnswerBinding
import com.bumptech.glide.Glide

class WendaAnswerListAdapter : RecyclerView.Adapter<WendaAnswerListAdapter.InnerHolder>() {

    private var mWendaThumbpClickListener: WendaAnswerListAdapter.OnWendaAnswerThumbpClickListener? =
        null
    private val wendaAnswerList = arrayListOf<WendaAnswer.Data>()

    class InnerHolder(var itemWendaAnswerBinding: ItemWendaAnswerBinding) :
        RecyclerView.ViewHolder(itemWendaAnswerBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemWendaAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return wendaAnswerList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemWendaAnswerBinding.apply {
            val wendaSubCommentAdapter = WendaSubCommentAdapter()
            wendaSubCommentsList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = wendaSubCommentAdapter
            }
            answerWebView.settings.useWideViewPort = true
            answerWebView.settings.loadWithOverviewMode = true
            answerWebView.settings.textZoom = 300
            answerWebView.settings.javaScriptEnabled = true
            with(wendaAnswerList[position]) {
                userName.text = nickname
                if (vip) {
                    userName.setTextColor(holder.itemView.resources.getColor(R.color.fff50cbb))
                } else {
                    userName.setTextColor(holder.itemView.resources.getColor(R.color.ff999999))
                }
                createTime.text = publishTimeText
                answerWebView.loadData(content, "text/html", "UTF-8")
                wendaThumbpNum.text = thumbUp.toString()
                Glide.with(holder.itemView).load(avatar).into(userAvatar)
                wendaSubCommentAdapter.setData(wendaSubComments)
            }
            isThumbpPart.setOnClickListener {
                //todo:问题回答点赞
                mWendaThumbpClickListener?.onThumbpClick()
            }
        }
    }

    fun setOnWendaAnswerThumbpClickListener(listener: OnWendaAnswerThumbpClickListener) {
        this.mWendaThumbpClickListener = listener
    }

    interface OnWendaAnswerThumbpClickListener {
        fun onThumbpClick()
    }

    fun setData(it: MutableList<WendaAnswer.Data>) {
        wendaAnswerList.clear()
        wendaAnswerList.addAll(it)
        notifyDataSetChanged()
    }
}