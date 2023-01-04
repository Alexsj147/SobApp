package com.alex.sobapp.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.sobapp.adapter.CommentsAdapter
import com.alex.sobapp.apiService.domain.MomentComment
import com.alex.sobapp.apiService.domain.SubComment
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentDynamicCommentsBinding
import com.alex.sobapp.utils.KeyboardControl
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.CommentsViewModel
import com.alex.sobapp.viewmodel.ReplyMomentViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_comments_pop.*

class DynamicCommentsFragment : BaseFragment<FragmentDynamicCommentsBinding>() {

    private var dynamicCommentId: String? = ""
    private var lastPosition = 0
    private var currentPosition = 0
    private var currentTargetUserId: String = ""
    private var currentCommentId: String = ""


    private val commentAdapter by lazy {
        CommentsAdapter()
    }

    private val commentsViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(CommentsViewModel::class.java)
    }

    private val replyMomentViewModel by lazy {
        ReplyMomentViewModel()
    }

    //override fun getPageLayoutId(): Int {
    //    return R.layout.fragment_dynamic_comments
    //}

    override fun initEvent() {
        dynamicCommentId = arguments?.getString("commentId", "")
        Log.d("myLog", "commentId is $dynamicCommentId")
    }

    override fun initView() {
        switchUIByState(LoadState.SUCCESS)
        binding.commentsListRv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
        }
        val shp = BaseApplication.getShp()
        val userAvatarPath = shp?.getString("userAvatar", "")
        Glide.with(requireContext()).load(userAvatarPath)
            .into(binding.itemCommentsPart.commentsUserAvatar)
    }

    override fun initObserver() {
        commentsViewModel.apply {
            contentValue.observe(this@DynamicCommentsFragment, Observer {
                commentAdapter.setData(it)
            })
        }.loadComments(dynamicCommentId!!)
    }

    private fun refresh() {
        commentsViewModel.loadComments(dynamicCommentId!!)
        //commentAdapter.notifyDataSetChanged()
    }

    override fun initListener() {
        binding.commentsBack.setOnClickListener {
            findNavController().navigateUp()

        }
        /*binding.refreshButton.setOnClickListener {
            refresh()
        }*/
        binding.releaseCommentButton.setOnClickListener {
            //显示隐藏底部评论输入框
            binding.apply {
                val itemVisibility = itemCommentsPart.root.visibility
                val commentVisibility = replyCommentPart.visibility
                if (commentVisibility == View.VISIBLE) {
                    replyCommentPart.visibility = View.GONE
                }
                if (itemVisibility == View.VISIBLE) {
                    itemCommentsPart.root.visibility = View.GONE
                } else {
                    itemCommentsPart.root.visibility = View.VISIBLE
                }
            }


        }
        commentAdapter.controlSubCommentAndCommentClick(object :
            CommentsAdapter.OnControlSubCommentAndComment {
            override fun controlShow() {
                binding.apply {
                    val visibility = replyCommentPart.visibility
                    if (visibility == View.VISIBLE) {
                        replyCommentPart.visibility = View.GONE
                    }
                }
            }
        })

        commentAdapter.setOnShowCommentReplyPart(object : CommentsAdapter.OnShowCommentReplyPart {
            override fun showCommentReplyPart(
                positions: Int,
                nickname: String,
                targetUserId: String,
                commentId: String
            ) {
                currentPosition = positions
                currentTargetUserId=targetUserId
                currentCommentId=commentId
                binding.apply {
                    replyCommentInputBox.hint = "回复@$nickname"
                    val visibility = replyCommentPart.visibility
                    if (currentPosition == lastPosition) {
                        if (visibility == View.GONE) {
                            replyCommentPart.visibility = View.VISIBLE
                        }
                        if (visibility == View.VISIBLE) {
                            replyCommentPart.visibility = View.GONE
                        }
                    } else {
                        if (visibility == View.GONE) {
                            replyCommentPart.visibility = View.VISIBLE
                        }
                        if (visibility == View.VISIBLE) {
                            //Log.d("myLog","修改回复对象")
                            replyCommentInputBox.hint = "回复@$nickname"
                        }
                    }
                }
                lastPosition = currentPosition
            }

        })
        commentAdapter.setOnShowMainCommentReply(object : CommentsAdapter.OnShowMainCommentReply {
            override fun showMainCommentReply() {
                //显示隐藏底部评论输入框
                val visibility = binding.itemCommentsPart.root.visibility
                if (visibility == View.VISIBLE) {
                    binding.itemCommentsPart.root.visibility = View.GONE
                }
            }

        })
        binding.replyCommentButton.setOnClickListener {
            //回复评论
            val subCommentText = binding.replyCommentInputBox.text.toString().trim()
            val subComment =
                SubComment(subCommentText, dynamicCommentId!!, currentTargetUserId, currentCommentId)
            Log.d("myLog", "Comment is $subComment")
            replyMomentViewModel.replySubComment(subComment)
            //todo:回复评论后界面刷新
            refresh()
        }
        commentAdapter.setOnResponseClickListener(object : CommentsAdapter.OnResponseClickListener {
            override fun onClick(
                targetUserId: String,
                commentId: String,
                subCommentText: String
            ) {
                val subComment =
                    SubComment(subCommentText, dynamicCommentId!!, targetUserId, commentId)
                Log.d("myLog", "subComment is $subComment")
                replyMomentViewModel.replySubComment(subComment)
                //todo:回复评论后界面刷新
                refresh()
            }
        })
        binding.itemCommentsPart.submitResponseComments.setOnClickListener {
            val commentsText = comments_input.text.toString()
            val momentComment = MomentComment(dynamicCommentId!!, commentsText)
            Log.d("myLog", "momentComment is $momentComment")
            replyMomentViewModel.replyMoment(momentComment)
            binding.itemCommentsPart.commentsInput.setText("")
            KeyboardControl.hideKeyboard(it)
            binding.itemCommentsPart.commentsInput.clearFocus()
            //todo:发表评论（评论动态）后界面刷新
            refresh()
        }


    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentDynamicCommentsBinding {
        return FragmentDynamicCommentsBinding.inflate(inflater, viewGroup, false)
    }
}