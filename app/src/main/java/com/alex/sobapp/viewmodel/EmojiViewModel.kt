package com.alex.sobapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex.sobapp.utils.Constants.TAG

class EmojiViewModel : ViewModel() {

    val recentEmojiList = MutableLiveData<ArrayList<Int>>()
    fun getRecentEmoji(): MutableLiveData<ArrayList<Int>> {
        return recentEmojiList
    }

    fun addRecentEmoji(emojiId: Int) {
        val oldList = recentEmojiList.value ?: arrayListOf()
        //Log.d(TAG, "addRecentEmoji: start oldList is $oldList")
        if (oldList.contains(emojiId)) {
            oldList.remove(emojiId)
            oldList.add(0, emojiId)
        } else {
            if (oldList.size < 6) {
                oldList.add(0, emojiId)
            } else {
                oldList.removeAt(5)
                oldList.add(0, emojiId)
            }
        }
        //oldList.add(0, emojiId)
        //Log.d(TAG, "addRecentEmoji: end oldList is $oldList")
        recentEmojiList.postValue(oldList)
    }

    fun setRecentEmoji(recentList: ArrayList<Int>) {
        //Log.d(TAG, "addRecentEmoji: start recentList is $recentList")
        recentEmojiList.postValue(recentList)
        //Log.d(TAG, "addRecentEmoji: end recentList is $recentList")
    }

}