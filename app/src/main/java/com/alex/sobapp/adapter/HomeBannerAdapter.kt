package com.alex.sobapp.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.alex.sobapp.apiService.domain.HomeBannerList
import com.bumptech.glide.Glide

class HomeBannerAdapter : PagerAdapter() {

    private val homeBannerList = arrayListOf<HomeBannerList.Data>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        val realPosition = position % homeBannerList.size
        val picUrl = homeBannerList[realPosition].picUrl
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(container.context).load(picUrl).into(imageView)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 50
    }

    fun setData(it: MutableList<HomeBannerList.Data>) {
        homeBannerList.clear()
        homeBannerList.addAll(it)
        notifyDataSetChanged()
    }
}