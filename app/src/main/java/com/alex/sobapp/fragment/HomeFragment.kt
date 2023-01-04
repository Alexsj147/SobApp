package com.alex.sobapp.fragment

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.adapter.HomeBannerAdapter
import com.alex.sobapp.adapter.HomeContentAdapter
import com.alex.sobapp.adapter.HomeGuideAdapter
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.custom.AutoLoopViewPager
import com.alex.sobapp.databinding.FragmentHomeBinding
import com.alex.sobapp.utils.ClickHelper
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.utils.SizeUtils
import com.alex.sobapp.viewmodel.CategoryListViewModel
import com.alex.sobapp.viewmodel.ClassifiedViewModel
import com.alex.sobapp.viewmodel.HomeBannerViewModel
import com.alex.sobapp.viewmodel.RecommendViewModel
import com.scwang.smart.refresh.footer.BallPulseFooter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    //private val mImmersionProxy = ImmersionProxy(this)

    private val guideAdapter by lazy {
        HomeGuideAdapter()
    }
    private val contentAdapter by lazy {
        HomeContentAdapter()
    }

    private val categoryListViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(CategoryListViewModel::class.java)
    }

    private val recommendViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(RecommendViewModel::class.java)
    }

    private val classifiedViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ClassifiedViewModel::class.java)
    }

    private val homeBannerViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(HomeBannerViewModel::class.java)
    }

    private val homeBannerAdapter by lazy {
        HomeBannerAdapter()
    }

    private val currentTypePosition = MutableLiveData<Int>()
    private val currentCategoryId = MutableLiveData<String>()
    //private var homeBannerViewPager: AutoLoopViewPager? = null

    //override fun getPageLayoutId(): Int {
    //    return R.layout.fragment_home
    //}

    override fun initEvent() {

    }

    override fun initView() {
        //Log.d("myLog", "binding is ${binding.root}")
        switchUIByState(LoadState.LOADING)
        binding.homeGuideRv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = guideAdapter
        }
        binding.homeContentRefresh.run {
            setEnableLoadMore(true)
            setEnableRefresh(false)
            setRefreshFooter(BallPulseFooter(context))
            setOnLoadMoreListener {
                if (currentTypePosition.value == 0) {
                    recommendViewModel.loadMoreContent()
                } else {
                    classifiedViewModel.loadMoreContent(currentCategoryId.value!!)
                }
            }
        }
        binding.homeContentRv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = contentAdapter
            addItemDecoration(
                object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.apply {
                            val paddingV: Int = SizeUtils.dp2px(context, 2.0f)
                            val paddingH: Int = SizeUtils.dp2px(context, 1.0f)
                            top = paddingV
                            left = paddingH
                            right = paddingH
                            //bottom = paddingV
                        }
                    }
                }
            )
        }
        //homeBannerViewPager = binding.homeBannerViewPager
        //rootView.home_banner_view_pager.run {
        //
        //}
    }

    override fun initObserver() {
        categoryListViewModel.apply {
            contentValue.observe(this@HomeFragment, Observer {
                guideAdapter.setData(it)
                recommendViewModel.loadContent()
                guideAdapter.updateSelectBg("0")
            })
            loadState.observe(this@HomeFragment, Observer {
                when (it) {
                    LoadState.LOADING -> {
                        switchUIByState(LoadState.LOADING)
                    }
                    LoadState.EMPTY -> {
                        switchUIByState(LoadState.EMPTY)
                    }
                    LoadState.ERROR -> {
                        switchUIByState(LoadState.ERROR)
                    }
                    LoadState.SUCCESS -> {
                        switchUIByState(LoadState.SUCCESS)
                    }
                    else -> {
                        binding.homeContentRefresh.home_content_refresh.finishLoadMore()
                    }
                }
            })
        }.loadCategory()
        recommendViewModel.apply {
            contentValue.observe(this@HomeFragment, Observer {
                contentAdapter.setData(it)
                currentTypePosition.value = 0
            })
            loadState.observe(this@HomeFragment, Observer {
                when (it) {
                    LoadState.LOADING -> {
                        switchUIByState(LoadState.LOADING)
                    }
                    LoadState.EMPTY -> {
                        switchUIByState(LoadState.EMPTY)
                    }
                    LoadState.ERROR -> {
                        switchUIByState(LoadState.ERROR)
                    }
                    LoadState.SUCCESS -> {
                        switchUIByState(LoadState.SUCCESS)
                    }
                    else -> {
                        binding.homeContentRefresh.home_content_refresh.finishLoadMore()
                    }
                }
                when (it) {
                    LoadState.LOADING_MORE_EMPTY -> {
                        Toast.makeText(context, "已经加载了所有的内容", Toast.LENGTH_SHORT).show()
                    }
                    LoadState.LOADING_MORE_ERROR -> {
                        Toast.makeText(context, "网络不佳，请稍后重试", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }
                }
            })
        }
        classifiedViewModel.apply {
            contentValue.observe(this@HomeFragment, Observer {
                contentAdapter.setData(it)
            })
            loadState.observe(this@HomeFragment, Observer {
                when (it) {
                    LoadState.LOADING -> {
                        switchUIByState(LoadState.LOADING)
                    }
                    LoadState.EMPTY -> {
                        switchUIByState(LoadState.EMPTY)
                    }
                    LoadState.ERROR -> {
                        switchUIByState(LoadState.ERROR)
                    }
                    else -> {
                        switchUIByState(LoadState.SUCCESS)

                    }
                }
                when (it) {
                    LoadState.LOADING_MORE_SUCCESS -> {
                        binding.homeContentRefresh.finishLoadMore()
                    }
                    LoadState.LOADING_MORE_EMPTY -> {
                        Toast.makeText(context, "已经加载了所有的内容", Toast.LENGTH_SHORT).show()
                    }
                    LoadState.LOADING_MORE_ERROR -> {
                        Toast.makeText(context, "网络不佳，请稍后重试", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }
                }
            })
        }
        homeBannerViewModel.apply {
            contentList.observe(this@HomeFragment, Observer {
                homeBannerAdapter.setData(it)
                binding.homeBannerViewPager.adapter = homeBannerAdapter
            })
        }.loadBanner()
    }

    override fun initListener() {
        guideAdapter.setOnItemClickListener(object : HomeGuideAdapter.OnItemClickListener {
            override fun onClick(id: String, position: Int) {
                if (!ClickHelper.isFastDoubleClick()) {
                    //加载右边的数据
                    println("当前的id：$id")
                    println("当前的position：$position")
                    if (position == 0) {
                        recommendViewModel.loadContent()
                        currentTypePosition.value = 0
                        //guideAdapter.updateSelectBg("0")
                    } else {
                        classifiedViewModel.loadContent(id)
                        currentTypePosition.value = position
                        currentCategoryId.value = id
                    }
                    guideAdapter.updateSelectBg(id)
                    binding.homeContentRv.scrollToPosition(0)//回到顶部
                }
            }

        })
        contentAdapter.setOnItemClickListener(object : HomeContentAdapter.OnItemClickListener {
            override fun onClick(id: String) {
                if (!ClickHelper.isFastDoubleClick()) {
                    //跳转至文章详情界面
                    val bundle = Bundle()
                    bundle.putString("articleId", id)
                    findNavController().navigate(R.id.toArticleFragment, bundle)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.homeBannerViewPager.startLoop()

    }

    override fun onPause() {
        super.onPause()
        binding.homeBannerViewPager.stopLoop()
    }

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, viewGroup, false)
    }

/*    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("test", "onCreate")
        mImmersionProxy.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("test", "onActivityCreated")
        mImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("test", "onResume")
        mImmersionProxy.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("test", "onPause")
        mImmersionProxy.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("test", "onDestroy")
        mImmersionProxy.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mImmersionProxy.onConfigurationChanged(newConfig)
    }


    override fun onLazyAfterView() {

    }

    override fun onInvisible() {

    }

    override fun onLazyBeforeView() {

    }


    override fun onVisible() {

    }

    override fun immersionBarEnabled(): Boolean {
        Log.d("test", "immersionBarEnabled is false")
        return false
    }

    override fun initImmersionBar() {
        Log.d("test", "Home initImmersionBar")
        //ImmersionBar.with(this,true)
        //    .statusBarDarkFont(true)
        //    .init()
    }*/
}