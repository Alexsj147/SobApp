package com.alex.sobapp.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.sobapp.R
import com.alex.sobapp.adapter.ArticleCommentAdapter
import com.alex.sobapp.adapter.RecommendArticleAdapter
import com.alex.sobapp.apiService.domain.ArticleComments
import com.alex.sobapp.apiService.domain.ArticleContent
import com.alex.sobapp.apiService.domain.RecommendArticle
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentArticleBinding
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.utils.SizeUtils
import com.alex.sobapp.viewmodel.ArticleCommentViewModel
import com.alex.sobapp.viewmodel.ArticleViewModel
import com.alex.sobapp.viewmodel.RecommendArticleViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.coroutineScope
import net.nightwhistler.htmlspanner.HtmlSpanner

class ArticleFragment : BaseFragment<FragmentArticleBinding>() {

    private var articleId: String? = null

    private val articleViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ArticleViewModel::class.java)
    }

    private val articleCommentViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ArticleCommentViewModel::class.java)
    }

    private val recommendArticleViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            RecommendArticleViewModel::class.java
        )
    }

    private val articleCommentAdapter by lazy {
        ArticleCommentAdapter()
    }
    private val recommendArticleAdapter by lazy {
        RecommendArticleAdapter()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentArticleBinding {
        return FragmentArticleBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        articleId = arguments?.getString("articleId", "")
    }

    override fun initView() {
        switchUIByState(LoadState.SUCCESS)
        //println("当前文章的articleId is $articleId")
        binding.articleContent.settings.useWideViewPort = true
        binding.articleContent.settings.loadWithOverviewMode = true
        binding.articleContent.settings.textZoom = 100
        binding.articleContent.settings.javaScriptEnabled = true
        //binding.articleContent.settings.defaultFontSize = SizeUtils.dp2px(requireContext(), 14f)
        //关注作者
        binding.focusButton.text = getString(R.string.focus_author)
        binding.focusButton.setTextColor(resources.getColor(R.color.white))
        binding.focusButton.background = resources.getDrawable(R.drawable.author_focus_bg)
        binding.focusButton.isSelected = false
        //评论列表
        binding.articleCommentRv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = articleCommentAdapter
        }
        binding.recommendArticleRv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = recommendArticleAdapter
        }
    }

    override fun initObserver() {
        articleViewModel.apply {
            contentValue.observe(this@ArticleFragment, Observer {
                //加载文章详情
                loadContent(it)
            })
        }.loadArticle(articleId!!)
        articleCommentViewModel.apply {
            contentValue.observe(this@ArticleFragment, Observer {
                //加载文章评论
                loadArticleCommentList(it)
            })
        }.loadArticleComments(articleId!!)
        recommendArticleViewModel.apply {
            contentValue.observe(this@ArticleFragment, Observer {
                //加载相关推荐文章
                loadRecommendArticle(it)
            })
        }.loadArticleComments(articleId!!)
    }

    private fun loadRecommendArticle(it: MutableList<RecommendArticle.Data>) {
        recommendArticleAdapter.setData(it)
    }

    private fun loadArticleCommentList(it: MutableList<ArticleComments.Content>) {
        articleCommentAdapter.setData(it)
    }

    override fun initListener() {
        binding.articleBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.focusButton.setOnClickListener {
            val hasFocus = binding.focusButton.isSelected
            if (!hasFocus) {
                binding.focusButton.text = getString(R.string.has_focus_author)
                binding.focusButton.setTextColor(resources.getColor(R.color.ff67c23a))
                binding.focusButton.background =
                    resources.getDrawable(R.drawable.author_has_focus_bg)
                binding.focusButton.isSelected = true
            } else {
                binding.focusButton.text = getString(R.string.focus_author)
                binding.focusButton.setTextColor(resources.getColor(R.color.white))
                binding.focusButton.background = resources.getDrawable(R.drawable.author_focus_bg)
                binding.focusButton.isSelected = false
            }
        }
    }

    private fun loadContent(it: ArticleContent) {
        //Html展示文章
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //    binding.articleContent.text = Html.fromHtml(it.content, FROM_HTML_MODE_COMPACT)
        //} else {
        //    binding.articleContent.text = Html.fromHtml(it.content)
        //}

        //webView展示文章
        //binding.articleContent.loadData(it.content, "text/html", "UTF-8")
        binding.articleContent.loadUrl("file:///android_asset/vue/index.html?id=$articleId")

        //markdown展示文章
        //val markdown = Markwon.builder(requireContext())
        //    .usePlugin(GlideImagesPlugin.create(requireContext()))
        //    .usePlugin(GlideImagesPlugin.create(Glide.with(this)))
        //    .usePlugin(GlideImagesPlugin.create(object : GlideImagesPlugin.GlideStore {
        //        override fun cancel(target: Target<*>) {
        //            Glide.with(this@ArticleFragment).clear(target)
        //        }
        //        override fun load(drawable: AsyncDrawable): RequestBuilder<Drawable> {
        //            return Glide.with(this@ArticleFragment).load(drawable.destination)
        //        }
        //    }))
        //    .build()
        //markdown.setMarkdown(binding.articleContent, it.content)

        //htmlSpanner
        //val handler = @SuppressLint("HandlerLeak")
        //object : Handler() {
        //    override fun handleMessage(msg: Message) {
        //        super.handleMessage(msg)
        //        val data = msg.data
        //        val content = data.getString("article")
        //        binding.articleContent.text = content
        //    }
        //}
        //Thread(Runnable {
        //    val article = HtmlSpanner().fromHtml(it.content)
        //    val message = Message()
        //    val bundle = Bundle()
        //    bundle.putString("article", article.toString())
        //    message.data = bundle
        //    handler.sendMessage(message)
        //}).start()


        Glide.with(this)
            .load(it.covers[0])
            .into(binding.articleCover)
        Glide.with(this)
            .load(it.avatar)
            .into(binding.userAvatar)
        binding.userNickName.text = it.nickname
        binding.createTimeTv.text = "发表于" + it.createTime
        binding.viewsNum.text = it.viewCount.toString()

        //原创说明
        val spannableString = SpannableString("本文由 ${it.nickname} 原创发布于 阳光沙滩 ，未经作者授权，禁止转载")
        val colorSpan = ForegroundColorSpan(Color.parseColor("#ff333333"))
        val end = 4 + it.nickname.length
        spannableString.setSpan(colorSpan, 4, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        binding.originalDesc.text = spannableString
    }

}