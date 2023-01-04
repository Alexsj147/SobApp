package com.alex.sobapp.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.databinding.ItemLargeImageBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_photo.*

class LargeImageAdapter : RecyclerView.Adapter<LargeImageAdapter.InnerHolder>() {

    private val imageList = arrayListOf<String>()

    class InnerHolder(var itemLargeImageBinding: ItemLargeImageBinding) :
        RecyclerView.ViewHolder(itemLargeImageBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val itemBinding =
            ItemLargeImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemLargeImageBinding.apply {
            //Log.d("myLog","imageUrl is ${imageList[position].trim()}")
            shimmerLayoutPhoto.apply {
                setShimmerColor(0x55ffffff)
                setShimmerAngle(0)
                startShimmerAnimation()
            }
            Glide.with(holder.itemView.context).load(imageList[position].trim())
                .placeholder(R.mipmap.default_img)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false.also { shimmerLayoutPhoto.stopShimmerAnimation() }
                    }

                })
                .into(photoView)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setData(it: ArrayList<String>) {
        imageList.clear()
        imageList.addAll(it)
        notifyDataSetChanged()
    }

}