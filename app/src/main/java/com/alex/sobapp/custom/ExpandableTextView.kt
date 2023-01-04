package com.alex.sobapp.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.alex.sobapp.R
import kotlinx.android.synthetic.main.view_expandable.view.*
import java.nio.charset.Charset
import kotlin.math.log

class ExpandableTextView : LinearLayout {


    /** 最大显示行数（默认 5 行） */
    private var mMaxLine: Int = 5;

    /** 显示的内容 */
    private var mContent: CharSequence = ""

    /** 当前是否已是 "全文" 状态 */
    private var mIsExpansion: Boolean = false

    constructor(context: Context?) :
            this(context, null)

    constructor(context: Context?, attributeSet: AttributeSet?) :
            this(context, attributeSet, 0)


    constructor(context: Context?, attributeSet: AttributeSet?, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr) {
        initView(context, attributeSet)
    }

    fun getTextSize(): Float {
        return tv_content.textSize
    }

    private fun initView(
        context: Context?,
        attributeSet: AttributeSet?
    ) {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_expandable, this)
        //mContentTextView = findViewById(R.id.tv_content);
        //mExpansionButton = findViewById(R.id.v_expansion);
        // 监听文本控件的布局绘制
        tv_content.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (tv_content.width == 0) {
                    return
                }
                tv_content.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setTextContent(
                    mContent
                )
            }
        })
        // "全文/收起" 按钮点击监听
        v_expansion.setOnClickListener {
            toggleExpansionStatus()
        }

    }

    private fun toggleExpansionStatus() {
        // 切换状态
        mIsExpansion = !mIsExpansion;

        // 更新内容和切换按钮的显示
        if (mIsExpansion) {
            v_expansion.text = "收起"                       // 全文状态, 按钮显示 "收起"
            tv_content.maxLines = Integer.MAX_VALUE        // 全文状态, 行数设置为最大
        } else {
            v_expansion.text = "展开";                       // 收起状态, 按钮显示 "全文"
            tv_content.maxLines = mMaxLine                 // 收起状态, 最大显示指定的行数
        }
    }

    fun setMaxLine(maxLine: Int) {
        mMaxLine = maxLine
        setTextContent(mContent)
    }

    fun setTextContent(text: CharSequence) {
        mContent = text
        //Log.d("myLog", "mcontent is $mContent")
        // 文本控件有宽度时（绘制成功后）才能获取到文本显示的所需要的行数,
        // 如果控件还没有被绘制, 等监听到绘制成功后再设置文本
        if (tv_content.width == 0) {
            return
        }
        tv_content.maxLines = Int.MAX_VALUE
        tv_content.text = mContent
        val lineCount = tv_content.lineCount
        if (lineCount > mMaxLine) {
            v_expansion.visibility = View.VISIBLE
            v_expansion.text = "展开"
            tv_content.maxLines = mMaxLine
            mIsExpansion = false
        } else {
            v_expansion.text = ""
            v_expansion.visibility = View.GONE
        }
    }
}