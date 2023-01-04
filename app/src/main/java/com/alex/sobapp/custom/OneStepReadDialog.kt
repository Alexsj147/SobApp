package com.alex.sobapp.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.alex.sobapp.databinding.OneStepReadDialogBinding

class OneStepReadDialog(context: Context) : Dialog(context) {

    private var mCancelClickListener: OnCancelClickListener? = null
    private var mConfirmClickListener: OnConfirmClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = OneStepReadDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(binding)
        initListener(binding)
    }

    fun setOnCancelClickListener(listener: OnCancelClickListener?) {
        mCancelClickListener = listener
    }

    interface OnCancelClickListener {
        fun onCancelClick()
    }

    fun setOnConfirmClickListener(listener: OnConfirmClickListener?) {
        mConfirmClickListener = listener
    }

    interface OnConfirmClickListener {
        fun onConfirmClick()
    }

    private fun initListener(binding: OneStepReadDialogBinding) {
        binding.oneStepReadCancel.setOnClickListener { //取消
            Log.d("myLog", "用户点击了取消")
            mCancelClickListener?.onCancelClick()
        }
        binding.oneStepReadConfirm.setOnClickListener { //确认
            Log.d("myLog", "用户点击了确认")
            mConfirmClickListener?.onConfirmClick()
        }
    }

    private fun initView(binding: OneStepReadDialogBinding) {
        binding.apply {
            oneStepReadTitle.text = "全部已读"
            oneStepReadContent.text = "确定全部消息标记为已读吗？"
            oneStepReadCancel.text = "取消"
            oneStepReadConfirm.text = "确认"
        }
    }
}
