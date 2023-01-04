package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.apiService.domain.CategoryList
import com.alex.sobapp.databinding.ItemHomeGuideBinding
import kotlinx.android.synthetic.main.item_home_guide.view.*

class HomeGuideAdapter : RecyclerView.Adapter<HomeGuideAdapter.InnerHolder>() {

    private var mItemClickListener: OnItemClickListener? = null
    private val categoryList = arrayListOf<CategoryList.Data>()
    private val currentId = MutableLiveData<String>()

    class InnerHolder(var itemHomeGuideBinding: ItemHomeGuideBinding) :
        RecyclerView.ViewHolder(itemHomeGuideBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        //val itemView =
        //    LayoutInflater.from(parent.context).inflate(R.layout.item_home_guide, parent, false)
        //return InnerHolder(itemView)
        val itemBinding =
            ItemHomeGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onClick(id: String, position: Int)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemHomeGuideBinding.apply {
            with(categoryList[position]) {
                categoryNameTv.text = categoryName
            }

        }
        //holder.itemView.apply {
        //    with(categoryList[position]) {
        //        category_name_tv.text = categoryName
        //        //categoryNameTv.text = categoryName
        //    }
        //}
//        if (position == 0) {
//            println("position is $position")
//            println("currentPosition is ${currentId.value}")
//            holder.itemView.isSelected = true
//        } else {
        if (currentId.value == categoryList[position].id) {
            println("position is $position")
            println("currentId is ${currentId.value}")
            holder.itemHomeGuideBinding.root.isSelected = true
            //}
        } else {
            holder.itemHomeGuideBinding.root.isSelected = false
        }
        holder.itemHomeGuideBinding.root.setOnClickListener {
            val id = categoryList[position].id
            mItemClickListener?.onClick(id, position)
        }

    }


    fun setData(it: MutableList<CategoryList.Data>) {
        categoryList.clear()
        categoryList.addAll(it)
        notifyDataSetChanged()
    }

    fun updateSelectBg(position: String) {
        currentId.value = position
        notifyDataSetChanged()
    }
}