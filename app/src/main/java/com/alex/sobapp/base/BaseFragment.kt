package com.alex.sobapp.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.alex.sobapp.R
import com.alex.sobapp.databinding.FragmentContainerBinding
import com.alex.sobapp.utils.LoadState
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_loading.view.*
import kotlin.math.log

abstract class BaseFragment<viewBinding : ViewBinding> : Fragment() {


    //private var rootView: ViewGroup? = null
    private var _binding: viewBinding? = null
    protected val binding get() = _binding!!
    private var baseContainer: FrameLayout? = null
    //protected val mActivity = FragmentActivity()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ImmersionBar.with(this).statusBarDarkFont(true).init()
        val rootView = getRootView(inflater, container)
        //val containerView = rootView?.findViewById<ViewGroup>(R.id.fragment_container)
        //setUpView(rootView, containerView, inflater)
        //_binding = getBinding(layoutInflater, container)
        //Log.d("myLog", "viewBinding init...")
        //var rootView = _binding.root
        //var root = _binding!!.root
        //root = getRootView(inflater, container)
        baseContainer = rootView.findViewById<FrameLayout>(R.id.fragment_container)
        setUpView(baseContainer, inflater)
        //设置相关的事件
        initEvent()
        initView()
        initObserver()
        initListener()
        return rootView
    }

    protected abstract fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): viewBinding

    /**
     * 添加控件相关事件
     */
    open fun initListener() {

    }

    /**
     * 观察数据变化
     */
    open fun initObserver() {

    }

    /**
     * 界面初始化
     */
    open fun initView() {

    }

    /**
     * 加载初始数据
     */
    open fun initEvent() {

    }


    private var successView: View? = null
    private var loadingView: View? = null
    private var errorView: View? = null
    private var emptyView: View? = null

    private fun setUpView(

        container: ViewGroup?,
        inflater: LayoutInflater
    ) {
        //成功的View
        _binding = getBinding(inflater, container)
        successView = _binding!!.root
        baseContainer?.addView(successView)
        //加载中的View
        loadingView = createLoadingView(inflater, container)
        baseContainer?.addView(loadingView)
        //网络错误的View
        errorView = createErrorView(inflater, container)
        errorView?.setOnClickListener {
            OnRetryClick()
        }
        baseContainer?.addView(errorView)
        //数据为空的界面
        emptyView = createEmptyView(inflater, container)
        baseContainer?.addView(emptyView)
        switchUIByState(LoadState.NONE)
        //initView(successView!!)
        //initView()
    }

    open fun OnRetryClick() {

    }


    //private fun subCreateView(inflater: LayoutInflater, container: ViewGroup?): View? {
    //    return inflater.inflate(getPageLayoutId(), container, false)
    //}

    //abstract fun getPageLayoutId(): Int

    //根据状态显示UI
    fun switchUIByState(state: LoadState) {
        successView?.visibility = if (state == LoadState.SUCCESS) View.VISIBLE else View.GONE
        loadingView?.visibility = if (state == LoadState.LOADING) View.VISIBLE else View.GONE
        errorView?.visibility = if (state == LoadState.ERROR) View.VISIBLE else View.GONE
        emptyView?.visibility = if (state == LoadState.EMPTY) View.VISIBLE else View.GONE
        if (state == LoadState.NONE) {
            successView?.visibility = View.GONE
            loadingView?.visibility = View.GONE
            errorView?.visibility = View.GONE
            emptyView?.visibility = View.GONE
        }
    }

    open fun createEmptyView(inflater: LayoutInflater, rootView: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_empty, rootView, false)
    }

    open fun createErrorView(inflater: LayoutInflater, rootView: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_error, rootView, false)
    }

    /**
     * 如果子类需要改变，覆盖即可
     */
    open fun createLoadingView(inflater: LayoutInflater, rootView: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_loading, rootView, false)
    }

    private fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_container, container, false) as View
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        release()
    }

    open fun release() {

    }


}