package com.alex.sobapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardControl {
    companion object{
        fun hideKeyboard(view:View){
            val manager: InputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        fun showKeyboard(view: View){
            val manager: InputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(view, 0)
        }
    }
}