package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.databinding.ItemTitleImageBinding
import com.bumptech.glide.Glide

class TitleImageAdapter : RecyclerView.Adapter<TitleImageAdapter.InnerHolder>() {

    private var mImageItemClickListener: OnImageItemClickListener? = null
    private val titleImages = arrayListOf<String>()
    private var mNickname: String = ""
    private var num: Int = 0

    class InnerHolder(var itemTitleImageBinding: ItemTitleImageBinding) :
        RecyclerView.ViewHolder(itemTitleImageBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        //val itemView =
        //    LayoutInflater.from(parent.context).inflate(R.layout.item_title_image, parent, false)
        //return InnerHolder(itemView)
        val itemBinding =
            ItemTitleImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return titleImages.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        num++
        with(titleImages[position]) {
            //println("第$num 次 当前 $position : $mNickname 的图片url is  $this")
            Glide.with(holder.itemTitleImageBinding.root.context)
                .load(this)
                .placeholder(R.mipmap.default_img)
                .into(holder.itemTitleImageBinding.titleImage)
        }
        //val imageUrl = titleImages[position]
        holder.itemView.setOnClickListener {
            mImageItemClickListener?.onImageClick(position)
        }

    }

    fun setOnImageItemClickListener(listener: OnImageItemClickListener) {
        this.mImageItemClickListener = listener
    }

    interface OnImageItemClickListener {
        fun onImageClick(currentPosition: Int)
    }

    fun setData(images: List<String>, nickname: String) {
        mNickname = nickname
        titleImages.clear()
        titleImages.addAll(images)
        //println("当前TitleImagesAdapter titleImages  is $titleImages")
        notifyDataSetChanged()
    }
}