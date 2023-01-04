package com.alex.sobapp.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.alex.sobapp.R
import kotlinx.android.synthetic.main.item_comments_response_pop.view.*


class CommentsResponsePop(context: Context, nickName: String) : PopupWindow(
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
) {

    private var mSubmitClickListener: OnSubmitClickListener? = null

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
            .inflate(R.layout.item_comments_response_pop, null)
        contentView = mPopView
        //设置动画
        animationStyle = R.style.pop_animation
        initView(mPopView, nickName)
        initListener(mPopView)
    }

    private fun initView(mPopView: View, nickName: String) {
        mPopView.response_comments_input.hint = "回复@${nickName}"
    }

    private fun initListener(mPopView: View) {
        mPopView.submit_response_comments.setOnClickListener {
            val text = mPopView.response_comments_input.text.toString()
            Log.d("myLog", "提交评论:$text")
            mSubmitClickListener?.onClick(text)
        }
    }

    fun setOnSubmitClickListener(listener: OnSubmitClickListener) {
        this.mSubmitClickListener = listener
    }

    interface OnSubmitClickListener {
        fun onClick(text: String)
    }
}
