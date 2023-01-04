package com.alex.sobapp.custom

import com.alex.sobapp.apiService.domain.ImageItem

class PickerConfig private constructor() {
    //private fun PickerConfig() {}

    //private var sPickerConfig: PickerConfig? = null
    //fun getInstance(): PickerConfig? {
    //    if (sPickerConfig == null) {
    //        sPickerConfig = PickerConfig()
    //    }
    //    return sPickerConfig
    //}

    companion object {
        val sPickerConfig: PickerConfig by lazy {
            PickerConfig()
        }
    }

    private var maxSelectedCount = 3
    private var mImagesSelectedFinishedListener: OnImagesSelectedFinishedListener? = null
    private var mSingleImageFinishedListener: OnSingleImageFinishedListener? = null
    //private var mVideosSelectedFinishedListener: OnVideosSelectedFinishedListener? = null

    fun getMaxSelectedCount(): Int {
        return maxSelectedCount
    }

    fun setMaxSelectedCount(maxSelectedCount: Int) {
        this.maxSelectedCount = maxSelectedCount
    }

    //多个图片
    fun getImagesSelectedFinishedListener(): OnImagesSelectedFinishedListener? {
        return mImagesSelectedFinishedListener
    }

    fun setOnImagesSelectedFinishedListener(listener: OnImagesSelectedFinishedListener?) {
        mImagesSelectedFinishedListener = listener
    }

    interface OnImagesSelectedFinishedListener {
        fun onSelectedImagesFinished(result: List<ImageItem>)
    }

    //单个图片
    fun getSingleImageFinishedListener(): OnSingleImageFinishedListener? {
        return mSingleImageFinishedListener
    }

    fun setOnSingleImageFinishedListener(listener: OnSingleImageFinishedListener?) {
        mSingleImageFinishedListener = listener
    }

    interface OnSingleImageFinishedListener {
        fun onSingleImageFinished(result: List<ImageItem>)
    }

    //视频
    //fun getVideosSelectedFinishedListener(): OnVideosSelectedFinishedListener? {
    //    return mVideosSelectedFinishedListener
    //}
//
    //fun setOnVideosSelectedFinishedListener(listener: OnVideosSelectedFinishedListener?) {
    //    mVideosSelectedFinishedListener = listener
    //}
//
    //interface OnVideosSelectedFinishedListener {
    //    fun onSelectedVideosFinished(result: List<VideoItem?>?)
    //}
}