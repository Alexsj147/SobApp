package com.alex.sobapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.DynamicComments
import com.alex.sobapp.databinding.ItemCommentsListTipsBinding
import com.alex.sobapp.databinding.ItemDynamicCommentsBinding
import com.alex.sobapp.utils.KeyboardControl
import com.bumptech.glide.Glide

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.InnerHolder>() {

    private var mControlClick: OnControlSubCommentAndComment? = null
    private var mShowPartClick: OnShowCommentReplyPart? = null
    private var mShowMainClick: OnShowMainCommentReply? = null
    private var mResponseClickListener: OnResponseClickListener? = null
    private val commentsList = arrayListOf<DynamicComments.Comments>()
    private val VIEW_TYPE_HEADER = 1
    private val VIEW_TYPE_BODY = 0
    private val VIEW_TYPE_FOOTER = 2
    private var createNum: Int = 0
    private var itemDynamicCommentsBinding: ItemDynamicCommentsBinding? = null
    private var itemCommentsListTipsBinding: ItemCommentsListTipsBinding? = null
    private var lastPosition = 0
    private var currentPosition = 0
    private var currentTargetUserId: String = ""
    private var currentCommentId: String = ""

    class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        var innerHolder = InnerHolder(parent.rootView)
        //if (viewType == VIEW_TYPE_HEADER) {
        //    val itemView = LayoutInflater.from(parent.context)
        //        .inflate(R.layout.item_no_comments_list_tips, parent, false)
        //    innerHolder = InnerHolder(itemView)
        //}
        if (viewType == VIEW_TYPE_BODY) {
            //val itemView = LayoutInflater.from(parent.context)
            //    .inflate(R.layout.item_dynamic_comments, parent, false)
            itemDynamicCommentsBinding = ItemDynamicCommentsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            innerHolder = InnerHolder(itemDynamicCommentsBinding!!.root)
        } else {
            if (viewType == VIEW_TYPE_FOOTER) {
                //val itemView = LayoutInflater.from(parent.context)
                //    .inflate(R.layout.item_comments_list_tips, parent, false)
                itemCommentsListTipsBinding = ItemCommentsListTipsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                innerHolder = InnerHolder(itemCommentsListTipsBinding!!.root)
            }
        }
        return innerHolder
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> {
                VIEW_TYPE_FOOTER
            }
            else -> {
                VIEW_TYPE_BODY
            }
        }
    }

    override fun getItemCount(): Int {
        return commentsList.size + 1
    }

    fun setOnResponseClickListener(listener: OnResponseClickListener) {
        this.mResponseClickListener = listener
    }

    interface OnResponseClickListener {
        fun onClick(
            targetUserId: String,
            commentId: String,
            subCommentText: String
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, positions: Int) {
        //println("itemCount == $itemCount")
        createNum++
        //println("createNum is $createNum")
        if (positions == itemCount - 1) {
            //println("footer positions is $positions")
            if (createNum == 1) {
                itemCommentsListTipsBinding!!.commentsTipsText.text = ""
            } else {
                if (commentsList.size == 0) {
                    itemCommentsListTipsBinding!!.commentsTipsText.text = "~还没有评论呢，你第一个评论吧~"
                } else {
                    itemCommentsListTipsBinding!!.commentsTipsText.text = "~没有更多内容了~"
                }
            }

        } else {
            //println("body positions is $positions")
            itemDynamicCommentsBinding!!.apply {
                val root = this.root
                val subCommentsAdapter = SubCommentsAdapter()
                subCommentsRv.run {
                    layoutManager = LinearLayoutManager(context)
                    adapter = subCommentsAdapter
                }
//                subCommentsAdapter.setOnSubResponseClickListener(object :
//                    SubCommentsAdapter.OnSubResponseClickListener {
//                    override fun onClick(
//                        nickname: String,
//                        targetUserId: String,
//                        commentId: String,
//                        subCommentText: String
//                    ) {
//                        /*mResponseClickListener?.onClick(
//                            nickname,
//                            targetUserId,
//                            commentId,
//                            subCommentText
//                        )*/
//                    }
//
//                })
                with(commentsList[positions]) {
                    Glide.with(root.context).load(avatar).into(userAvatar)
                    nickNameTv.text = nickname
                    if (vip) {
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
                    articleContent.text = content
                    createTimeTv.text = createTime
                    if (subComments.isEmpty()) {

                    } else {
                        subCommentsAdapter.setData(subComments)
                    }
                }
                subCommentsAdapter.setOnShowMainCommentReply(object :
                    SubCommentsAdapter.OnShowMainCommentReply {
                    override fun showMainCommentReply() {
                        mShowMainClick?.showMainCommentReply()
                    }
                })
                subCommentsAdapter.setOnShowSubReply(object : SubCommentsAdapter.OnShowSubReply {
                    override fun showSubReply(
                        positions: Int,
                        nickname: String,
                        targetUserId: String,
                        commentId: String
                    ) {
                        currentPosition = positions
                        currentTargetUserId=targetUserId
                        currentCommentId=commentId
                        this@apply.apply {
                            replySubCommentInputBox.hint = "回复@$nickname"
                            val visibility = replySubCommentPart.visibility
                            if (currentPosition == lastPosition) {
                                if (visibility == View.GONE) {
                                    replySubCommentPart.visibility = View.VISIBLE
                                }
                                if (visibility == View.VISIBLE) {
                                    replySubCommentPart.visibility = View.GONE
                                }
                            } else {
                                if (visibility == View.GONE) {
                                    replySubCommentPart.visibility = View.VISIBLE
                                }
                                if (visibility == View.VISIBLE) {
                                    //Log.d("myLog","修改回复对象")
                                    replySubCommentInputBox.hint = "回复@$nickname"
                                }
                            }
                        }
                        //判断commentPart是否隐藏
                        mControlClick?.controlShow()

                        lastPosition = currentPosition
                    }

                })
                replySubCommentButton.setOnClickListener {
                    //回复子评论
                    val subCommentText = replySubCommentInputBox.text.toString().trim()
                    mResponseClickListener?.onClick(currentTargetUserId,currentCommentId,subCommentText)
                }


                responseButton.setOnClickListener {
                    //回复
                    //Log.d("myLog", "Comments 输入你的回复")
                    val nickname = commentsList[positions].nickname
                    val targetUserId = commentsList[positions].userId
                    val commentId = commentsList[positions].id
                    mShowPartClick?.showCommentReplyPart(positions, nickname,targetUserId,commentId)
                    //Log.d("myLog", "当前的位置 ： $positions")
                    val visibility = replySubCommentPart.visibility
                    if (visibility == View.VISIBLE) {
                        replySubCommentPart.visibility = View.GONE
                    }
                    mShowMainClick?.showMainCommentReply()

                }
                /*replyCommentButton.setOnClickListener {
                    val nickname = commentsList[positions].nickname
                    val targetUserId = commentsList[positions].userId
                    val commentId = commentsList[positions].id
                    val commentText = this.replyCommentInputBox.text.toString().trim()
                    //todo:回复评论
                    mResponseClickListener?.onClick(
                        nickname,
                        targetUserId,
                        commentId,
                        commentText
                    )
                }*/
            }
        }
        holder.itemView.setOnClickListener {
            KeyboardControl.hideKeyboard(it)
        }
    }

    /**
     * 控制subComment与Comment的显示/隐藏
     */
    fun controlSubCommentAndCommentClick(click: OnControlSubCommentAndComment) {
        this.mControlClick = click
    }

    interface OnControlSubCommentAndComment {
        fun controlShow()
    }

    /**
     * 显示/隐藏底部评论输入框
     */
    fun setOnShowCommentReplyPart(showPartClick: OnShowCommentReplyPart) {
        this.mShowPartClick = showPartClick
    }

    interface OnShowCommentReplyPart {
        fun showCommentReplyPart(
            positions: Int,
            nickname: String,
            targetUserId: String,
            commentId: String
        )
    }

    /**
     * 显示/隐藏发表评论输入框
     */
    fun setOnShowMainCommentReply(showMainClick: OnShowMainCommentReply) {
        this.mShowMainClick = showMainClick
    }

    interface OnShowMainCommentReply {
        fun showMainCommentReply()
    }

    /*if (positions == itemCount - 1) {

    } else if (positions == itemCount - 2) {

    } else {

    }*/


    fun setData(it: MutableList<DynamicComments.Comments>) {
        commentsList.clear()
        commentsList.addAll(it)
        notifyDataSetChanged()
    }

}