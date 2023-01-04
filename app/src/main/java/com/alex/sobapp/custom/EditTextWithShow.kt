package com.alex.sobapp.custom

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.alex.sobapp.R

class EditTextWithShow @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr) {
    //private val iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24)
    private var iconDrawable: Drawable? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.EditTextWithShow, 0, 0
        ).apply {
            try {
                val iconID = getResourceId(R.styleable.EditTextWithShow_showIcon, 0)
                if (iconID != 0) {
                    iconDrawable = ContextCompat.getDrawable(context, iconID)
                }
            } finally {
                recycle()
            }
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        toggleClearIcon()
    }

    /**
     * 按下x时清除文本内容
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { e ->
            iconDrawable?.let {
                if (e.action == MotionEvent.ACTION_DOWN
                    && e.x > width - it.intrinsicWidth - 20
                    && e.x < width + 20
                    && e.y > height / 2 - it.intrinsicHeight / 2 - 20
                    && e.y < height / 2 + it.intrinsicHeight / 2 + 20
                ) {
                    transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    transformationMethod = PasswordTransformationMethod.getInstance()
                }
                text?.length?.let { textLength -> setSelection(textLength) }
            }
        }
        performClick()
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    /**
     * 失去焦点时隐藏X按钮
     */
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        toggleClearIcon()
    }

    /**
     * 显示/隐藏 x 按钮
     */
    private fun toggleClearIcon() {
        val icon = if (isFocused && text?.isNotEmpty() == true) iconDrawable else null
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null)
    }
}