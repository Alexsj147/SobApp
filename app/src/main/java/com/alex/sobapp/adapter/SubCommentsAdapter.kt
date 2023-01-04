package com.alex.sobapp.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.DynamicComments
import com.alex.sobapp.databinding.ItemSubCommentsBinding
import com.alex.sobapp.utils.KeyboardControl
import com.bumptech.glide.Glide

class SubCommentsAdapter : RecyclerView.Adapter<SubCommentsAdapter.InnerHolder>() {

    private var mShowSubReply: OnShowSubReply? = null
    private var mShowMainClick: OnShowMainCommentReply? = null
    private var mSubResponseClickListener: OnSubResponseClickListener? = null
    private val subCommentsList = arrayListOf<DynamicComments.Comments.SubComment>()

    class InnerHolder(var itemSubCommentsBinding: ItemSubCommentsBinding) :
        RecyclerView.ViewHolder(itemSubCommentsBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        //val itemView =
        //    LayoutInflater.from(parent.context).inflate(R.layout.item_sub_comments, parent, false)
        //return InnerHolder(itemView)
        val itemBinding =
            ItemSubCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InnerHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return subCommentsList.size
    }

    fun setOnSubResponseClickListener(listener: OnSubResponseClickListener) {
        this.mSubResponseClickListener = listener
    }

    interface OnSubResponseClickListener {
        fun onClick(
            nickname: String,
            targetUserId: String,
            commentId: String,
            subCommentText: String
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, positions: Int) {
        holder.itemSubCommentsBinding.apply {
            holder.itemView.setOnClickListener {
                KeyboardControl.hideKeyboard(it)
            }
            val root = this.root
            with(subCommentsList[positions]) {
                Glide.with(root.context).load(avatar).into(userAvatar)
                nickNameTv.text = nickname
                if (targetUserIsVip) {
                    nickNameTv.setTextColor(root.resources.getColor(R.color.fff50cbb))
                }
                if (position == null) {
                    userPosition.text = ""
                } else {
                    userPosition.text = position
                }
                if (company == null) {
                    userCompany.text = ""
                } else {
                    userCompany.text = "@$company"
                }
                val spannableString = SpannableString("回复@$targetUserNickname")
                val colorSpan = ForegroundColorSpan(root.resources.getColor(R.color.ff1296db))
                spannableString.setSpan(
                    colorSpan,
                    2,
                    spannableString.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                targetUserNickNameTv.text = spannableString
                articleContent.text = content
                createTimeTv.text = createTime
            }

            subResponseButton.setOnClickListener {
                //拉起评论输入框
                //Log.d("myLog", "subComments 输入你的回复")
                val targetUserId = subCommentsList[positions].userId
                val nickname = subCommentsList[positions].nickname
                val commentId = subCommentsList[positions].commentId
                mShowSubReply?.showSubReply(positions, nickname,targetUserId,commentId)
                mShowMainClick?.showMainCommentReply()
            }
//            replySubCommentButton.setOnClickListener {
//                val targetUserId = subCommentsList[positions].userId
//                val nickname = subCommentsList[positions].nickname
//                val commentId = subCommentsList[positions].commentId
//                val subCommentText = replySubCommentInputBox.text.toString().trim()
//                //todo:回复子评论
//                mSubResponseClickListener?.onClick(
//                    nickname,
//                    targetUserId,
//                    commentId,
//                    subCommentText
//                )
//            }
        }
    }


    fun setOnShowSubReply(showSubReply: OnShowSubReply) {
        this.mShowSubReply = showSubReply
    }

    interface OnShowSubReply {
        fun showSubReply(
            positions: Int,
            nickname: String,
            targetUserId: String,
            commentId: String
        )
    }


    fun setOnShowMainCommentReply(showMainClick: OnShowMainCommentReply) {
        this.mShowMainClick = showMainClick
    }

    interface OnShowMainCommentReply {
        fun showMainCommentReply()
    }

    fun setData(subComments: List<DynamicComments.Comments.SubComment>) {
        subCommentsList.clear()
        subCommentsList.addAll(subComments)
        notifyDataSetChanged()
    }



}