package com.alex.sobapp.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.*
import com.alex.sobapp.databinding.ItemMineInfoBinding
import com.alex.sobapp.utils.Constants
import com.bumptech.glide.Glide

class MineInfoAdapter : RecyclerView.Adapter<MineInfoAdapter.InnerHolder>() {

    private var mToDetailClickListener: OnToDetailClickListener? = null
    private val systemViewType = 1
    private val normalViewType = 0
    private var msgFrom = ""
    private val articleCommentList = arrayListOf<MineArticleComment.Content>()
    private val dynamicCommentList = arrayListOf<MineDynamicComment.Content>()
    private val wendaList = arrayListOf<MineWendaList.Content>()
    private val atMeInfoList = arrayListOf<MineAtMeInfo.Content>()
    private val thumbUpList = arrayListOf<MineThumbUpList.Content>()
    private val systemInfoList = arrayListOf<SystemInfo.Content>()

    class InnerHolder(val itemMineInfoBinding: ItemMineInfoBinding) :
        RecyclerView.ViewHolder(itemMineInfoBinding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {

        val itemBinding =
            ItemMineInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)

    }

    override fun getItemCount(): Int {
        when (msgFrom) {
            Constants.FROM_ARTICLE_COMMENT -> {
                return articleCommentList.size
            }
            Constants.FROM_DYNAMIC_COMMENT -> {
                return dynamicCommentList.size
            }
            Constants.FROM_QUESTION_ANSWER -> {
                return wendaList.size
            }
            Constants.FROM_AT_ME -> {
                return atMeInfoList.size
            }
            Constants.FROM_THUMB_UP_ME -> {
                return thumbUpList.size
            }
            Constants.FROM_SYSTEM_INFO -> {
                return systemInfoList.size
            }
            else -> {

            }
        }
        return 0
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.itemMineInfoBinding.apply {
            if (msgFrom == Constants.FROM_SYSTEM_INFO) {
                systemInfoPart.visibility = View.VISIBLE
                mineInfoPart.visibility = View.GONE
            } else {
                systemInfoPart.visibility = View.GONE
                mineInfoPart.visibility = View.VISIBLE
            }
            when (msgFrom) {
                Constants.FROM_ARTICLE_COMMENT -> {
                    with(articleCommentList[position]) {
                        userName.text = nickname
                        replyTime.text = createTime
                        replyComment.text = content
                        toDetailButton.text = "??????????????????"
                        textTips.text = "????????????????????????"
                        Glide.with(holder.itemView).load(avatar).into(userAvatar)
                        //todo:???????????????????????????
                    }
                }
                Constants.FROM_DYNAMIC_COMMENT -> {
                    with(dynamicCommentList[position]) {
                        userName.text = nickname
                        replyTime.text = createTime
                        replyComment.text = content
                        textTips.text = "????????????????????????"
                        toDetailButton.text = "??????????????????"
                        Glide.with(holder.itemView).load(avatar).into(userAvatar)
                        //if (hasRead == "1"){
                        //    hasReadTip.setBackgroundResource(R.drawable.has_read_bg)
                        //    hasReadTip.text="??????"
                        //}else{
                        //    hasReadTip.setBackgroundResource(R.drawable.not_read_bg)
                        //    hasReadTip.text="??????"
                        //}

                    }
                    toDetailButton.setOnClickListener {
                        //todo:?????????????????????????????????
                        //?????????????????????????????????
                        mToDetailClickListener?.onClick(dynamicCommentList[position]._id)

                    }
                }
                Constants.FROM_QUESTION_ANSWER -> {
                    with(wendaList[position]) {
                        userName.text = nickname
                        replyTime.text = createTime
                        textTips.text = "????????????????????????"
                        toDetailButton.text = title
                        Glide.with(holder.itemView).load(avatar).into(userAvatar)
                        hasReadTip.visibility=View.GONE
                        //todo:?????????????????????????????????
                    }
                }
                Constants.FROM_AT_ME -> {
                    with(atMeInfoList[position]) {
                        userName.text = nickname
                        replyTime.text = publishTime
                        textTips.text = "????????????????????????"
                        toDetailButton.text = "??????????????????"
                        Glide.with(holder.itemView).load(avatar).into(userAvatar)
                        //todo:?????????????????????????????????
                    }
                }
                Constants.FROM_THUMB_UP_ME -> {
                    with(thumbUpList[position]) {
                        userName.text = nickname
                        replyTime.text = thumbTime
                        textTips.text = "?????????????????????"
                        toDetailButton.text = title
                        Glide.with(holder.itemView).load(avatar).into(userAvatar)
                        hasReadTip.visibility=View.GONE
                        //todo:?????????????????????????????????
                    }
                }
                Constants.FROM_SYSTEM_INFO -> {
                    with(systemInfoList[position]) {
                        systemTitle.text = title
                        systemTime.text = publishTime
                        systemContent.text = Html.fromHtml(content)
                        systemContent.setOnClickListener {
                            //todo:?????????????????????sunof?????????
                        }
                    }
                }
                else -> {

                }
            }

        }
    }

    interface OnToDetailClickListener {
        fun onClick(msgId: String)
    }

    fun setOnToDetailButtonClickListener(listener: OnToDetailClickListener) {
        this.mToDetailClickListener = listener
    }

    fun setArticleComment(
        it: MutableList<MineArticleComment.Content>,
        fromMsg: String
    ) {
        msgFrom = fromMsg
        articleCommentList.clear()
        articleCommentList.addAll(it)
        notifyDataSetChanged()
    }

    fun setDynamicComment(
        it: MutableList<MineDynamicComment.Content>,
        fromMsg: String
    ) {
        msgFrom = fromMsg
        dynamicCommentList.clear()
        dynamicCommentList.addAll(it)
        notifyDataSetChanged()
    }

    fun setWendaList(it: MutableList<MineWendaList.Content>, fromMsg: String) {
        msgFrom = fromMsg
        wendaList.clear()
        wendaList.addAll(it)
        notifyDataSetChanged()
    }

    fun setAtMeInfo(it: MutableList<MineAtMeInfo.Content>, fromMsg: String) {
        msgFrom = fromMsg
        atMeInfoList.clear()
        atMeInfoList.addAll(it)
        notifyDataSetChanged()
    }

    fun setThumpUpList(it: MutableList<MineThumbUpList.Content>, fromMsg: String) {
        msgFrom = fromMsg
        thumbUpList.clear()
        thumbUpList.addAll(it)
        notifyDataSetChanged()
    }

    fun setSystemInfo(it: MutableList<SystemInfo.Content>, fromMsg: String) {
        msgFrom = fromMsg
        systemInfoList.clear()
        systemInfoList.addAll(it)
        notifyDataSetChanged()
    }
}