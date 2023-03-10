package com.alex.sobapp.adapter

import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.ImageItem
import com.alex.sobapp.databinding.ItemPickerImageBinding
import com.alex.sobapp.utils.Constants.MULTI_SELECT_COUNT
import com.alex.sobapp.utils.SizeUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_picker_image.view.*
import kotlin.collections.ArrayList

class PickerAdapter : RecyclerView.Adapter<PickerAdapter.InnerHolder>() {

    private var mItemClickListener: OnItemClickListener? = null
    private var mItemSelectedChangeListener: OnItemSelectedChangeListener? = null
    private val imageItemList = arrayListOf<ImageItem>()
    private var mSelectedItem = arrayListOf<ImageItem>()



    private var maxSelectedCount: Int = MULTI_SELECT_COUNT

    fun getSelectedItem(): ArrayList<ImageItem> {
        return mSelectedItem
    }

    fun setSelectedItem(selectedItem: ArrayList<ImageItem>) {
        mSelectedItem = selectedItem
    }

    fun getMaxSelectedCount(): Int {
        return maxSelectedCount
    }

    fun setMaxSelectedCount(maxSelectedCount: Int) {
        this.maxSelectedCount = maxSelectedCount
    }

    class InnerHolder(var itemPickerImageBinding: ItemPickerImageBinding) :
        RecyclerView.ViewHolder(itemPickerImageBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        //val itemView =
        //    LayoutInflater.from(parent.context).inflate(R.layout.item_picker_image, parent, false)
        val itemBinding =
            ItemPickerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val point: Point = SizeUtils.getScreenSize(itemBinding.root.context)
        val layoutParams = RecyclerView.LayoutParams(point.x / 3, point.x / 3)
        itemBinding.root.layoutParams = layoutParams
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return imageItemList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val itemView = holder.itemPickerImageBinding.root
        val imageItem = imageItemList[position]
        val imageCover = itemView.image_cover
        val checkImage = itemView.image_check_box
        //checkBox.isClickable = false
        checkImage.setBackgroundResource(R.mipmap.image_check_normal)
        imageCover.visibility = View.GONE
        Glide.with(itemView.context)
            .load(imageItem.path)
            .placeholder(R.mipmap.default_img)
            .into(itemView.image_iv)
        //??????????????????????????????
        if (mSelectedItem.contains(imageItem)) {
            //??????????????????
            mSelectedItem.add(imageItem)
            //??????UI
            //checkBox.isChecked = false
            checkImage.setBackgroundResource(R.mipmap.image_checked)
            imageCover.visibility = View.VISIBLE
        } else {
            //?????????????????????
            mSelectedItem.remove(imageItem)
            //??????UI
            //checkBox.isChecked = true
            checkImage.setBackgroundResource(R.mipmap.image_check_normal)
            imageCover.visibility = View.GONE
        }
        //
        itemView.select_image_button.setOnClickListener {
            //??????????????????
            //????????????????????????
            //????????????????????????
            if (mSelectedItem.contains(imageItem)) {
                //?????????????????????
                mSelectedItem.remove(imageItem)
                //??????UI
                //checkBox.isChecked = true
                checkImage.setBackgroundResource(R.mipmap.image_check_normal)
                imageCover.visibility = View.GONE
            } else {
                if (mSelectedItem.size >= maxSelectedCount) {
                    Toast.makeText(
                        it.image_check_box.context,
                        "??????????????????" + maxSelectedCount + "?????????",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //??????????????????
                    mSelectedItem.add(imageItem)
                    //??????UI
                    //checkBox.isChecked = false
                    checkImage.setBackgroundResource(R.mipmap.image_checked)
                    imageCover.visibility = View.VISIBLE
                }
            }
            mItemSelectedChangeListener?.onItemSelectedChange(mSelectedItem)

        }
        itemView.setOnClickListener {
            mItemClickListener?.onItemClickListener(imageItem)
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClickListener(imageItem: ImageItem)
    }

    fun setOnItemSelectedChangeListener(listener: OnItemSelectedChangeListener) {
        this.mItemSelectedChangeListener = listener
    }

    interface OnItemSelectedChangeListener {
        fun onItemSelectedChange(selectedItem: List<ImageItem>)
    }

    fun release() {
        mSelectedItem.clear()
    }

    fun setData(mImageItems: ArrayList<ImageItem>) {
        imageItemList.clear()
        imageItemList.addAll(mImageItems)
        notifyDataSetChanged()
    }
}