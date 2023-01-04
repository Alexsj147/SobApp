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
import com.alex.sobapp.databinding.ItemExchangeBackgroundBinding

class ExchangeBgPop(context: Context) : PopupWindow(
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
) {

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
            .inflate(R.layout.item_exchange_background, null)
        val popBinding = ItemExchangeBackgroundBinding.bind(mPopView)
        contentView = mPopView
        //设置动画
        animationStyle = R.style.pop_slide_animation
        //initView(mPopView)
        initListener(popBinding)
    }

    private var mExchangeClickListener: OnExchangeClickListener? = null
    fun setOnExchangeClickListener(listener: OnExchangeClickListener) {
        this.mExchangeClickListener = listener
    }

    interface OnExchangeClickListener {
        fun onClick()
    }

    private fun initListener(popBinding: ItemExchangeBackgroundBinding) {
        popBinding.exchangeBackground.setOnClickListener {
            //更换背景
            mExchangeClickListener?.onClick()
        }
        popBinding.cancelExchange.setOnClickListener {
            //取消
            dismiss()
        }
    }

    private fun initView(mPopView: View) {

    }


}