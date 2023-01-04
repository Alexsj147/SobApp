package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.ImageItem
import com.alex.sobapp.databinding.ItemResultPicBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_result_pic.view.*
import java.util.ArrayList

class ResultPicListAdapter : RecyclerView.Adapter<ResultPicListAdapter.InnerHolder>() {

    private val resultList = arrayListOf<ImageItem>()

    class InnerHolder(var itemResultPicBinding: ItemResultPicBinding) :
        RecyclerView.ViewHolder(itemResultPicBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        //val itemView =
        //    LayoutInflater.from(parent.context).inflate(R.layout.item_result_pic, parent, false)
        //return InnerHolder(itemView)
        val itemBinding =
            ItemResultPicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val imagePath = resultList[position].path
        Glide.with(holder.itemResultPicBinding.root.context)
            .load(imagePath)
            .placeholder(R.mipmap.default_img)
            .into(holder.itemResultPicBinding.resultPicImage)
    }

    fun setData(resultPicList: ArrayList<ImageItem>) {
        resultList.clear()
        resultList.addAll(resultPicList)
        notifyDataSetChanged()
    }
}