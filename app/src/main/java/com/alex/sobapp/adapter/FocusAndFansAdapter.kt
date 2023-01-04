package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.FocusAndFansListInfo
import com.alex.sobapp.databinding.ItemFocusOrFansBinding
import com.bumptech.glide.Glide
import java.util.ArrayList

class FocusAndFansAdapter : RecyclerView.Adapter<FocusAndFansAdapter.InnerHolder>() {

    private var mFocusClickListener: OnFocusClickListener? = null
    private val mFocusAndFansList = arrayListOf<FocusAndFansListInfo.ListItem>()

    class InnerHolder(var itemFocusOrFansBinding: ItemFocusOrFansBinding) :
        RecyclerView.ViewHolder(itemFocusOrFansBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemFocusOrFansBinding =
            ItemFocusOrFansBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemFocusOrFansBinding)
    }

    override fun getItemCount(): Int {
        return mFocusAndFansList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemFocusOrFansBinding.apply {
            with(mFocusAndFansList[position]) {
                Glide.with(holder.itemView).load(avatar).into(userAvatar)
                userName.text = nickname
                userSign.text = sign
                //0表示没有关注对方，可以显示为：关注
                //1表示对方关注自己，可以显示为：回粉
                //2表示已经关注对方，可以显示为：已关注
                //3表示相互关注，可以显示为：相互关注
                when (relative) {
                    1 -> {
                        focusButton.isEnabled=true
                        focusButton.text = "回粉"
                        focusButton.setTextColor(holder.itemView.resources.getColor(R.color.fff24896))
                        focusButton.background =
                            holder.itemView.resources.getDrawable(R.drawable.re_focus_text_bg)
                    }
                    2 -> {
                        focusButton.isEnabled=true
                        focusButton.text = "已关注"
                        focusButton.setTextColor(holder.itemView.resources.getColor(R.color.ff1296db))
                        focusButton.background =
                            holder.itemView.resources.getDrawable(R.drawable.has_focus_text_bg)
                    }
                    3 -> {
                        focusButton.isEnabled=true
                        focusButton.text = "相互关注"
                        focusButton.setTextColor(holder.itemView.resources.getColor(R.color.ff42b018))
                        focusButton.background =
                            holder.itemView.resources.getDrawable(R.drawable.both_focus_text_bg)
                    }
                    else -> {
                        focusButton.isEnabled=false
                    }
                }
                focusButton.setOnClickListener {
                    //取消关注、回粉
                    mFocusClickListener?.onFocusClick(userId,relative)
                }
            }
        }
    }

    fun setOnFocusClickListener(listener: OnFocusClickListener) {
        this.mFocusClickListener = listener
    }

    interface OnFocusClickListener {
        fun onFocusClick(userId: String, relative: Int)
    }

    fun setData(focusAndFansList: ArrayList<FocusAndFansListInfo.ListItem>) {
        mFocusAndFansList.clear()
        mFocusAndFansList.addAll(focusAndFansList)
        notifyDataSetChanged()
    }
}