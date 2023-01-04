package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.databinding.ItemEmojiImageBinding
import com.alex.sobapp.utils.Constants.ALL_EMOJI_TYPE
import com.alex.sobapp.utils.Constants.RECENT_EMOJI_TYPE
import com.bumptech.glide.Glide
import java.util.ArrayList

class EmojiAdapter : RecyclerView.Adapter<EmojiAdapter.InnerHolder>() {

    private var mAllEmojiClickListener: OnAllEmojiItemClickListener? = null
    private val mAllEmojiList = arrayListOf<Int>()
    private val mRecentEmojiList = arrayListOf<Int>()
    private var mEmojiType = ""

    class InnerHolder(var itemEmojiImageBinding: ItemEmojiImageBinding) :
        RecyclerView.ViewHolder(itemEmojiImageBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemEmojiImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return if (mEmojiType == ALL_EMOJI_TYPE) {
            mAllEmojiList.size
        } else {
            mRecentEmojiList.size
        }
    }

    fun setOnAllEmojiItemClickListener(listener: OnAllEmojiItemClickListener) {
        this.mAllEmojiClickListener = listener
    }

    interface OnAllEmojiItemClickListener {
        fun onAllClick(emojiId: Int)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemEmojiImageBinding.apply {
            when (mEmojiType) {
                ALL_EMOJI_TYPE -> {
                    with(mAllEmojiList[position]) {
                        Glide.with(holder.itemView).load(this).into(emojiIcon)

                    }
                    holder.itemView.setOnClickListener {
                        //将表情添加进文本和最近使用
                        mAllEmojiClickListener?.onAllClick(mAllEmojiList[position])
                    }
                }
                RECENT_EMOJI_TYPE->{
                    with(mRecentEmojiList[position]){
                        Glide.with(holder.itemView).load(this).into(emojiIcon)
                    }
                    holder.itemView.setOnClickListener {
                        //将表情添加进文本和最近使用
                        mAllEmojiClickListener?.onAllClick(mRecentEmojiList[position])
                    }
                }
                else -> {
                }
            }

        }
    }

    fun setAllList(emojiList: ArrayList<Int>, emojiType: String) {
        mEmojiType = emojiType
        mAllEmojiList.clear()
        mAllEmojiList.addAll(emojiList)
        notifyDataSetChanged()
    }

    fun setRecentList(emojiList: ArrayList<Int>, emojiType: String) {
        mEmojiType = emojiType
        mRecentEmojiList.clear()
        mRecentEmojiList.addAll(emojiList)
        notifyDataSetChanged()
    }
}