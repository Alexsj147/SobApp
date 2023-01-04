package com.alex.sobapp.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.alex.sobapp.R
import com.alex.sobapp.databinding.ItemLinkPopBinding

class LinkPop(context: Context?) : PopupWindow(
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
) {

    private var mAddClickListener: OnAddLinkClickListener? = null

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
            .inflate(R.layout.item_link_pop, null)
        val popBinding = ItemLinkPopBinding.bind(mPopView)
        contentView = mPopView
        //设置动画
        animationStyle = R.style.pop_animation
        initView(popBinding)
        initListener(popBinding)
    }

    private fun initView(binding: ItemLinkPopBinding) {
        binding.addLinkButton.isEnabled = false
    }

    private fun initListener(binding: ItemLinkPopBinding) {
        binding.linkEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    binding.addLinkButton.isEnabled = true
                    binding.addLinkButton.background =
                        binding.root.resources.getDrawable(R.drawable.link_add_text_select_bg)
                } else {
                    binding.addLinkButton.isEnabled = false
                    binding.addLinkButton.background =
                        binding.root.resources.getDrawable(R.drawable.link_add_text_normal_bg)
                }
            }

        })
        binding.addLinkButton.setOnClickListener {
            val linkUrl = binding.linkEditText.text.toString().trim()
            mAddClickListener?.onClick(linkUrl)
        }
    }

    fun setOnAddLinkClickListener(listener: OnAddLinkClickListener) {
        this.mAddClickListener = listener
    }

    interface OnAddLinkClickListener {
        fun onClick(linkUrl: String)
    }
}