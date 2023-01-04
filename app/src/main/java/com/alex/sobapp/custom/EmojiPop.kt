package com.alex.sobapp.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import com.alex.sobapp.R
import com.alex.sobapp.adapter.EmojiAdapter
import com.alex.sobapp.apiService.EmojiList
import com.alex.sobapp.databinding.ItemEmojiPopBinding
import com.alex.sobapp.utils.Constants.ALL_EMOJI_TYPE
import com.alex.sobapp.utils.Constants.RECENT_EMOJI_TYPE
import java.util.ArrayList

class EmojiPop(context: Context?) :
    PopupWindow(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ) {

    private var mAllEmojiClickListener: OnAllEmojiClickListener? = null
    private val emojiRecentAdapter by lazy {
        EmojiAdapter()
    }
    private val emojiAllAdapter by lazy {
        EmojiAdapter()
    }
    private val emojiRecentList = arrayListOf<Int>()

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
            .inflate(R.layout.item_emoji_pop, null)
        val popBinding = ItemEmojiPopBinding.bind(mPopView)
        contentView = mPopView
        //设置动画
        animationStyle = R.style.pop_animation
        initView(popBinding)
        initListener(popBinding)
    }

    private fun initView(binding: ItemEmojiPopBinding) {
        binding.allEmojiList.apply {
            //列表适配
            layoutManager = GridLayoutManager(binding.root.context, 6)
            adapter = emojiAllAdapter
        }
        binding.recentEmojiList.apply {
            //列表适配
            layoutManager = GridLayoutManager(binding.root.context, 6)
            adapter = emojiRecentAdapter
        }
        emojiAllAdapter.setAllList(EmojiList.emojiList, ALL_EMOJI_TYPE)

        //emojiRecentAdapter.setRecentList()
    }

    fun setOnAllEmojiClickListener(listener: OnAllEmojiClickListener) {
        this.mAllEmojiClickListener = listener
    }

    interface OnAllEmojiClickListener {
        fun onAllClick(emojiId: Int)
    }

    private fun initListener(binding: ItemEmojiPopBinding) {
        emojiAllAdapter.setOnAllEmojiItemClickListener(object :
            EmojiAdapter.OnAllEmojiItemClickListener {
            override fun onAllClick(emojiId: Int) {
                mAllEmojiClickListener?.onAllClick(emojiId)
            }

        })
    }

    fun setRecentEmojiList(recentList: ArrayList<Int>) {
        emojiRecentList.clear()
        emojiRecentList.addAll(recentList)
        emojiRecentAdapter.setRecentList(emojiRecentList, RECENT_EMOJI_TYPE)
    }
}