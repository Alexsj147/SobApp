package com.alex.sobapp.adapter

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.MoYuInfoList
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.databinding.ItemMoyuListBinding
import com.alex.sobapp.utils.SizeUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_moyu_list.view.*


class MoYuListAdapter : RecyclerView.Adapter<MoYuListAdapter.InnerHolder>() {

    private var mImageClickListener: OnImageItemClickListener? = null
    private var mShareClickListener: OnShareClickListener? = null
    private var mThumbUpClickListener: OnThumbUpClickListener? = null
    private var mCommentsClickListener: OnCommentsClickListener? = null
    private val moYuList = arrayListOf<MoYuInfoList.MoYuItem>()
    private val maxLine: Int = 5
    private val titleImageList = arrayListOf<String>()
    private val shp = BaseApplication.getShp()

    //private val titleImageAdapter by lazy {
    //    TitleImageAdapter()
    //}

    class InnerHolder(var itemMoyuListBinding: ItemMoyuListBinding) :
        RecyclerView.ViewHolder(itemMoyuListBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        //val itemView =
        //    LayoutInflater.from(parent.context).inflate(R.layout.item_moyu_list, parent, false)
        //return InnerHolder(itemView)
        val itemBinding =
            ItemMoyuListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        //println(moYuList.size)
        return moYuList.size
    }


    fun setOnCommentsClickListener(listener: OnCommentsClickListener) {
        this.mCommentsClickListener = listener
    }

    interface OnCommentsClickListener {
        fun onClick(id: String)
    }

    fun setOnImageItemClickListener(listener: OnImageItemClickListener) {
        this.mImageClickListener = listener
    }

    interface OnImageItemClickListener {
        fun onImageClick(images: List<String>, currentPosition: Int)
    }

    override fun onBindViewHolder(holder: InnerHolder, positions: Int) {
        holder.itemMoyuListBinding.apply {
            val root = this.root
            val titleImageAdapter = TitleImageAdapter()
            titleImageRv.run {
                layoutManager = LinearLayoutManager(
                    holder.itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = titleImageAdapter
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.apply {
                            val paddingV: Int = SizeUtils.dp2px(context, 2.0f)
                            val paddingH: Int = SizeUtils.dp2px(context, 4.0f)
                            //top = paddingV
                            //left = paddingH
                            right = paddingH
                            //bottom = paddingV
                        }
                    }
                })
            }
            titleImageAdapter.setOnImageItemClickListener(object :
                TitleImageAdapter.OnImageItemClickListener {
                override fun onImageClick(currentPositon: Int) {
                    //查看大图
                    mImageClickListener?.onImageClick(moYuList[positions].images,currentPositon)
                }

            })
            with(moYuList[positions]) {
                Glide.with(holder.itemView.context).load(avatar).into(userAvatar)
                userName.text = nickname
                if (position == null) {
                    userPosition.text = "游民"
                } else {
                    userPosition.text = position
                }
                if (company == null) {
                    userCompany.text = "@无业"
                } else {
                    userCompany.text = "@$company"
                }
                createTimeTv.text = createTime
                //设置显示全文
                //val sp = Html.fromHtml(content, Html.ImageGetter { source ->
                //    Log.d(TAG, "需要加载的图片地址：$source")
                //    return@ImageGetter parseEmoji(
                //        source!!,
                //        articleText.getTextSize().toInt(),
                //        root.rootView
                //    )
                //}, null)
                val sp = Html.fromHtml(content, Html.ImageGetter { source ->
                    parseEmoji(
                        source!!,
                        articleText.getTextSize().toInt(),
                        root.rootView
                    )
                }, null)
                articleText.setTextContent(sp)
                articleText.setMaxLine(maxLine)
                userCommentNum.text = commentCount.toString()
                userThumbNum.text = thumbUpCount.toString()
                if (vip) {
                    userName.setTextColor(root.resources.getColor(R.color.fff50cbb))
                    vipBg.visibility = View.VISIBLE
                    vipIcon.visibility = View.VISIBLE
                } else {
                    vipBg.visibility = View.GONE
                    vipIcon.visibility = View.GONE
                }
                if (topicName == "" || topicName == null) {
                    topicNameTv.visibility = View.GONE
                } else {
                    topicNameTv.visibility = View.VISIBLE
                    topicNameTv.text = topicName
                }
                if (linkTitle == "" || linkUrl == "" || linkTitle == null || linkUrl == null) {
                    linkPart.visibility = View.GONE
                } else {
                    linkPart.visibility = View.VISIBLE
                    Glide.with(holder.itemView.context)
                        .load(linkCover)
                        .placeholder(R.mipmap.default_link)
                        .into(linkImage)
                    linkTitleTv.text = linkTitle
                    linkUrlTv.text = linkUrl
                }
                //val images = moYuList[positions].images
                //val nickname = moYuList[positions].nickname
                if (images.isEmpty()) {
                    titleImageRv.visibility = View.GONE
                    //println("当前list${positions}不可见")
                } else {
                    titleImageRv.visibility = View.VISIBLE
                    //println("当前list${positions}可见")
                    titleImageList.clear()
                    titleImageList.addAll(images)
                    //println("当前的list的size is ${titleImageList.size}")
                    titleImageAdapter.setData(titleImageList, nickname)
                }
                //显示点赞情况
                val userId = shp?.getString("userId", "")
                if (thumbUpList.contains(userId)) {
                    thumbUpCountIcon.setImageResource(R.mipmap.thumb3)
                    userThumbNum.setTextColor(root.resources.getColor(R.color.ff1296db))
                } else {
                    thumbUpCountIcon.setImageResource(R.mipmap.thumb_2)
                    userThumbNum.setTextColor(root.resources.getColor(R.color.ff666666))
                }
            }
        }
        holder.itemMoyuListBinding.root.comment_part.setOnClickListener {
            val id = moYuList[positions].id
            mCommentsClickListener?.onClick(id)
        }
        holder.itemMoyuListBinding.root.thumbUpCount_part.setOnClickListener {
            //点赞
            val momentId = moYuList[positions].id
            mThumbUpClickListener?.onClick(momentId, positions)
        }
        holder.itemMoyuListBinding.root.share_part.setOnClickListener {
            //分享
            mShareClickListener?.onClick()
        }
    }

    interface OnThumbUpClickListener {
        fun onClick(momentId: String, position: Int)
    }

    fun setOnThumbUpClickListener(listener: OnThumbUpClickListener) {
        this.mThumbUpClickListener = listener
    }

    interface OnShareClickListener {
        fun onClick()
    }

    fun setOnShareClickListener(listener: OnShareClickListener) {
        this.mShareClickListener = listener
    }


    fun setData(it: MutableList<MoYuInfoList.MoYuItem>) {
        moYuList.clear()
        moYuList.addAll(it)
        notifyDataSetChanged()
    }

    private fun parseEmoji(source: String, textSize: Int, view: View): Drawable? {
        //https://cdn.sunofbeaches.com/emoji/1.png
        //如果没有，返回null
        if (TextUtils.isEmpty(source)) {
            return null
        }
        //如果有，是否是阳光沙滩的地址cdn.sunofbeaches.com/emoji
        if (!source.contains("cdn.sunofbeaches.com/emoji")) {
            return null
        }
        //如果是，截取图片png的名字
        val start = source.lastIndexOf("/")
        val end = source.lastIndexOf(".")
        if (start == -1 || end == -1) {
            //出现-1情况就是找不到这个字符串，这样应影响到后面的截取api
            return null
        }
        val emojiName = source.substring(start + 1, end)
        //Log.d(TAG, "截取的表情名字：$emojiName")
        if (!TextUtils.isDigitsOnly(emojiName) || TextUtils.isEmpty(emojiName)) {
            //如果表情名字不是数字，那不是我们的表情，如果后面规则有改变，可以做同步调整
            return null
        }
        //判断1-130范围
        val emojiId = emojiName.toInt()
        if (emojiId < 0 || emojiId > 130) {
            return null
        }
        try {
            //因为图片名字是动态，这个需要反射获取内置表情
            val declaredField = R.mipmap::class.java.getDeclaredField("f$emojiName")
            val emojiDrawableId = declaredField.getInt(R.mipmap::class.java)
            val drawable =
                ContextCompat.getDrawable(view.context, emojiDrawableId) ?: return null
            //表情的大小和tv的文字大小一致,如果不设置就是宽高0/0
            drawable.setBounds(0, 0, textSize, textSize)
            //Log.d(TAG, "parseEmoji: drawable is $drawable")
            return drawable
        } catch (e: NoSuchFieldException) {
            e.printStackTrace();
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    fun changeStatus(position: Int) {
        notifyItemChanged(position)
    }
}