package com.alex.sobapp.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.adapter.TopicAdapter
import com.alex.sobapp.apiService.domain.TopicList
import com.alex.sobapp.databinding.ItemTopicPopBinding

class TopicPop(context: Context?) :
    PopupWindow(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ) {

    private var mTopicItemClickListener: OnTopicItemClickListener? = null
    private val topicAdapter by lazy {
        TopicAdapter()
    }

    init {
        //设置宽高
        //设置setOutsideTouchable时，先设置setBackgroundDrawable()
        //否则无法关闭
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isFocusable = true
        //setTouchable(true);
        isOutsideTouchable = true
        //加载view
        val mPopView = LayoutInflater.from(context)
            .inflate(R.layout.item_topic_pop, null)
        val popBinding = ItemTopicPopBinding.bind(mPopView)
        contentView = mPopView
        //设置动画
        animationStyle = R.style.pop_animation
        initView(popBinding)
        initListener(popBinding)

    }

    fun setOnTopicItemClickListener(listener: OnTopicItemClickListener) {
        this.mTopicItemClickListener = listener
    }

    interface OnTopicItemClickListener {
        fun onTopicItemClick(topicId: String)
    }

    private fun initListener(binding: ItemTopicPopBinding) {
        topicAdapter.setOnItemClickListener(object : TopicAdapter.OnItemClickListener {
            override fun onItemClick(topicId: String) {
                mTopicItemClickListener?.onTopicItemClick(topicId)
                dismiss()
            }

        })
    }

    private fun initView(binding: ItemTopicPopBinding) {
        binding.topicListRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = topicAdapter
        }
    }

    fun setDataList(list: MutableList<TopicList.Data>) {
        topicAdapter.setData(list)
    }
}