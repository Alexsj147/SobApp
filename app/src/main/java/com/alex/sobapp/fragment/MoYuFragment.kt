package com.alex.sobapp.fragment

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.R
import com.alex.sobapp.activity.ExchangeBgActivity
import com.alex.sobapp.adapter.MoYuListAdapter
import com.alex.sobapp.apiService.domain.ImageItem
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.custom.ExchangeBgPop
import com.alex.sobapp.custom.PickerConfig
import com.alex.sobapp.databinding.FragmentMoyuBinding
import com.alex.sobapp.utils.*
import com.alex.sobapp.utils.Constants.CROP_REQUEST_CODE
import com.alex.sobapp.utils.Constants.MOYU_BG_PATH
import com.alex.sobapp.utils.Constants.SINGLE_SELECT_COUNT
import com.alex.sobapp.viewmodel.MoYuInfoViewModel
import com.alex.sobapp.viewmodel.ThumbUpViewModel
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.absoluteValue

class MoYuFragment : BaseFragment<FragmentMoyuBinding>(),
    PickerConfig.OnSingleImageFinishedListener {

    private var totalDy: Int = 0
    private var currentSlide: Int = 0
    private var finalSlide: Int = 0
    private val shp = BaseApplication.getShp()
    private val shpEdit = BaseApplication.getShpEdit()

    private val mAdapter by lazy {
        MoYuListAdapter()
    }

    private val moYuInfoViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MoYuInfoViewModel::class.java)
    }

    private val mPickerConfig by lazy {
        PickerConfig.sPickerConfig
    }

    private val thumbUpViewModel by lazy {
        ThumbUpViewModel()
    }


    //override fun getPageLayoutId(): Int {
    //    return R.layout.fragment_moyu
    //}

    override fun initEvent() {
        mPickerConfig.setMaxSelectedCount(SINGLE_SELECT_COUNT)
        mPickerConfig.setOnSingleImageFinishedListener(this)
    }

    override fun initView() {
        switchUIByState(LoadState.LOADING)

        binding.moyuListRv.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(object : RecyclerView.ItemDecoration() {
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
            })
        }
        binding.moyuRefreshLayout.apply {
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setRefreshHeader(ClassicsHeader(context))
            setRefreshFooter(BallPulseFooter(context))
        }
    }


    override fun initObserver() {
        super.initObserver()
        moYuInfoViewModel.apply {
            contentValue.observe(this@MoYuFragment, Observer {
                mAdapter.setData(it)
            })
            loadState.observe(this@MoYuFragment, Observer {
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
                        binding.moyuRefreshLayout.finishLoadMore()
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
        }.loadContent(Constants.MOYU_TOPIC)
        thumbUpViewModel.apply {
            result.observe(this@MoYuFragment, Observer {
                //Log.d("myLog", "点赞返回值：${it.code}")
                //Log.d("myLog", "点赞返回值：${it.message}")
                //Log.d("myLog", "点赞返回值：${it.data}")
                //Log.d("myLog", "点赞返回值：${it.success}")
                if (it.success) {
                    Log.d("myLog", "点赞成功")
                }
            })
        }
    }

    override fun initListener() {
        binding.moyuAppBarLayout.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                val absoluteValue = verticalOffset.absoluteValue
                val alpha: Float
                currentSlide = absoluteValue
                //println("滑动距离：$currentSlide")
                when {
                    absoluteValue in 0..510 -> {
                        binding.moyuBg.alpha = 1f
                        binding.addDynamic.alpha = 1f
                        binding.moyuToolBar.alpha = 0f
                    }
                    absoluteValue in 0..560 -> {
                        binding.moyuToolBar.alpha = 0f
                    }
                    absoluteValue in 510..610 -> {
                        alpha = (absoluteValue - 510).toFloat() / 100f
                        //println("当前的moyu_bg 的 alpha is ${1 - alpha}")
                        binding.moyuBg.alpha = 1 - alpha
                        binding.addDynamic.alpha = 1 - alpha
                    }
                    absoluteValue in 560..660 -> {
                        alpha = (absoluteValue - 560).toFloat() / 100f
                        //println("当前的toolBar 的 alpha is $alpha")
                        binding.moyuToolBar.alpha = alpha
                    }
                    absoluteValue >= 610 -> {
                        binding.moyuBg.alpha = 0f
                        binding.addDynamic.alpha = 0f
                    }
                    absoluteValue >= 660 -> {
                        binding.moyuToolBar.alpha = 1f
                        binding.toolBarAddDynamic.alpha = 1f
                        binding.toolBarTitle.alpha = 1f
                    }
                    else -> {
                        binding.toolBarAddDynamic.alpha = 0f
                        binding.toolBarTitle.alpha = 0f
                    }
                }
            })
        mAdapter.setOnImageItemClickListener(object : MoYuListAdapter.OnImageItemClickListener {
            override fun onImageClick(images: List<String>, currentPosition: Int) {
                //todo:查看大图
                val imageList = images.toString().replace("[", "").replace("]","")
                val bundle = Bundle()
                bundle.putString("imageList", imageList)
                bundle.putInt("currentPosition", currentPosition)
                findNavController().navigate(R.id.toLargeImageFragment, bundle)
            }

        })
        mAdapter.setOnCommentsClickListener(object : MoYuListAdapter.OnCommentsClickListener {
            override fun onClick(id: String) {
                if (!ClickHelper.isFastDoubleClick()) {
                    //跳转至评论界面
                    val bundle = Bundle()
                    bundle.putString("commentId", id)
                    findNavController().navigate(R.id.toDynamicCommentsFragment, bundle)
                }
            }
        })
        mAdapter.setOnThumbUpClickListener(object : MoYuListAdapter.OnThumbUpClickListener {
            override fun onClick(momentId: String, position: Int) {
                //点赞
                thumbUpViewModel.thumbUp(momentId)
                //mAdapter.changeStatus(position)
            }

        })
        mAdapter.setOnShareClickListener(object : MoYuListAdapter.OnShareClickListener {
            override fun onClick() {
                //todo:分享
            }

        })
        binding.addDynamic.setOnClickListener {
            if (!ClickHelper.isFastDoubleClick()) {
                if (currentSlide < 610) {
                    findNavController().navigate(R.id.toReleaseDynamicFragment)
                }
            }

        }
        binding.toolBarAddDynamic.setOnClickListener {
            if (!ClickHelper.isFastDoubleClick()) {
                if (currentSlide > 610) {
                    findNavController().navigate(R.id.toReleaseDynamicFragment)
                }
            }

        }
        binding.moyuBg.setOnClickListener {
            //更改背景
            val exchangeBgPop = ExchangeBgPop(binding.root.context)
            exchangeBgPop.showAtLocation(binding.root, Gravity.BOTTOM, 0, 0)
            val lp: WindowManager.LayoutParams = activity?.window!!.attributes
            lp.alpha = 0.3f
            activity?.window!!.attributes = lp
            exchangeBgPop.setOnExchangeClickListener(object :
                ExchangeBgPop.OnExchangeClickListener {
                override fun onClick() {
                    //跳转之更换背景界面
                    exchangeBgPop.dismiss()
                    //findNavController().navigate(R.id.toExchangeBgFragment)
                    startActivity(Intent(requireContext(), ExchangeBgActivity::class.java))
                }

            })
            exchangeBgPop.setOnDismissListener {
                lp.alpha = 1f
                activity?.window!!.attributes = lp
            }
        }
        binding.moyuRefreshLayout.setOnRefreshListener {
            //todo:刷新
            Log.d("myLog", "触发了刷新")
            binding.moyuRefreshLayout.finishRefresh(2000)

        }
        binding.moyuRefreshLayout.setOnLoadMoreListener {
            //加载更多
            Log.d("myLog", "触发了加载更多")
            //binding.moyuRefreshLayout.finishLoadMore(2000)
            moYuInfoViewModel.loadMoreContent(Constants.MOYU_TOPIC)
        }
    }

    override fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentMoyuBinding {
        return FragmentMoyuBinding.inflate(inflater, viewGroup, false)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("myLog","requestCode is $requestCode  ||  resultCode is $resultCode")
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CROP_REQUEST_CODE -> {
                    Log.d("myLog", "brand is ${Build.BRAND}")
                    if (Build.BRAND == "Xiaomi") {
                        try {
                            val bitmap = BitmapFactory.decodeStream(
                                activity?.contentResolver?.openInputStream(mTempFile!!)
                            )
                            Glide.with(this).load(bitmap).into(binding.moyuBg)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    } else {
                        Log.d("myLog", "返回的data is $data")
                        val dataUri = data!!.data
                        Log.d("myLog", "dataUri is $dataUri")
                        //Bundle bundle = data.getExtras();
                        //Log.d("myLog", "bundle is " + bundle);
                        //if (bundle != null) {
                        //    Bitmap image = bundle.getParcelable("data");
                        //    Log.d("myLog", "返回的image is " + image);
                        var bitmap: Bitmap? = null
                        try {
                            bitmap =
                                MediaStore.Images.Media.getBitmap(
                                    activity?.contentResolver,
                                    dataUri
                                )
                            Glide.with(this).load(bitmap).into(binding.moyuBg)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                else -> {

                }
            }
        }
    }*/

    /**
     * 调用相机
     */
    /*private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                //说明成功咯
                println("result is $activityResult.data")
                val intentData = activityResult.data
                val bitmap = (intentData?.extras?.get("data")) as Bitmap
                val bitmapStr =
                    MediaStore.Images.Media.insertImage(activity?.contentResolver, bitmap, "", "")
                val parseUri = Uri.parse(bitmapStr)
                Log.d("myLog", "parseUri is $parseUri")
                //val cropIntent = createIntent(parseUri)
                val cropIntent = CreateIntent.create(parseUri)
                albumCropLauncher.launch(cropIntent)
            }
        }*/

    /**
     * 打开相册,选择图片
     */
    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activity_result ->
            if (activity_result.resultCode == Activity.RESULT_OK) {
                val uri = activity_result.data?.data
                Log.d("myLog", "data is $uri")
                var bitmap: Bitmap? = null
                var str: String? = ""
                Log.d("myLog", "uri is $uri")
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                    str = MediaStore.Images.Media.insertImage(
                        activity?.contentResolver,
                        bitmap,
                        "",
                        ""
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val parseUri = Uri.parse(str)
                Log.d("myLog", "parseUri is $parseUri")
                //---------------------------------------//
                //val cropIntent = createIntent(parseUri)
                val cropIntent = CreateIntent.create(parseUri)
                //调用系统裁切
                albumCropLauncher.launch(cropIntent)
            }
        }

    private fun createIntent(parseUri: Uri?): Intent {
        return Intent().apply {
            action = "com.android.camera.action.CROP"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            setDataAndType(parseUri, "image/*")
            putExtra("crop", "true")
            putExtra("aspectX", "5")
            putExtra("aspectY", "4")
            putExtra("outPutX", "375")
            putExtra("outPutY", "300")
            if (Build.BRAND == "Xiaomi") {
                mTempFile = Uri.parse(
                    "file://" + "/" + Environment.getExternalStorageDirectory().path
                            + "/" + System.currentTimeMillis() + ".jpg"
                )
                putExtra(MediaStore.EXTRA_OUTPUT, mTempFile)
                putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            }
            putExtra("return-data", "true")
        }
    }

    /**
     * 调用系统的裁切
     */
    private val albumCropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            //Log.d("myLog", "binding is $binding")
            Log.d("myLog", "resultCode is ${activityResult.resultCode}")
            if (activityResult.resultCode == Activity.RESULT_OK) {
                Log.d("myLog", "brand is ${Build.BRAND}")
                if (Build.BRAND == "Xiaomi") {
                    try {
                        val bitmap = BitmapFactory.decodeStream(
                            activity?.contentResolver?.openInputStream(mTempFile!!)
                        )
                        Glide.with(this).load(bitmap).into(binding.moyuBg)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    val dataUri = activityResult.data?.data
                    Log.d("myLog", "dataUri is $dataUri")
                    //Bundle bundle = data.getExtras();
                    //Log.d("myLog", "bundle is " + bundle);
                    //if (bundle != null) {
                    //    Bitmap image = bundle.getParcelable("data");
                    //    Log.d("myLog", "返回的image is " + image);
                    shpEdit?.putString(MOYU_BG_PATH, dataUri.toString())
                    shpEdit?.apply()
                    //var bitmap: Bitmap? = null
                    //try {
                    //    bitmap =
                    //        MediaStore.Images.Media.getBitmap(
                    //            activity?.contentResolver,
                    //            dataUri
                    //        )
                    //    //Glide.with(this).load(bitmap).into(binding.moyuBg)
                    //
                    //} catch (e: IOException) {
                    //    e.printStackTrace()
                    //}
                }
            }
        }

    private var mTempFile: Uri? = null
    private fun photoClip(uri: Uri?) {
        val intent = Intent()
        intent.action = "com.android.camera.action.CROP"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", "5")
        intent.putExtra("aspectY", "4")
        intent.putExtra("outPutX", "375")
        intent.putExtra("outPutY", "300")
        if (Build.BRAND == "Xiaomi") {
            mTempFile = Uri.parse(
                "file://" + "/" + Environment.getExternalStorageDirectory().path
                        + "/" + System.currentTimeMillis() + ".jpg"
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempFile)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        }
        intent.putExtra("return-data", "true")
        startActivityForResult(intent, CROP_REQUEST_CODE)
    }


    /**
     * 获取选择后的图片的数据
     */
    override fun onSingleImageFinished(result: List<ImageItem>) {
        Log.d("myLog", "MoYuFragment imageItem is ${result[0].path}")
        //shpEdit?.putString("moyuBg", result[0].path)
        //shpEdit?.apply()
        val mUri: Uri? = pathToUri(result[0].path)
        //val cropIntent = createIntent(mUri)
        val cropIntent = CreateIntent.create(mUri)
        albumCropLauncher.launch(cropIntent)
    }

    /**
     * 将获取的图片的path转换成uri
     */
    private fun pathToUri(path: String): Uri? {
        var uri: Uri? = null
        //val path = Uri.decode(path)
        //Log.d("myLog", "MoYuFragment parseUri is $path")
        val cr = activity?.contentResolver
        val buff = StringBuffer()
        buff.append("(")
            .append(MediaStore.Images.ImageColumns.DATA)
            .append("=")
            .append("'$path'")
            .append(")")
        val cur: Cursor? = cr?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.ImageColumns._ID),
            buff.toString(),
            null,
            null
        )
        var index = 0
        cur?.moveToFirst()
        while (!cur?.isAfterLast!!) {
            index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID)
            // set _id value
            index = cur.getInt(index)
            cur.moveToNext()
        }
        cur.close()
        if (index == 0) {
            //do nothing
        } else {
            val uriTemp = Uri
                .parse(
                    "content://media/external/images/media/"
                            + index
                )
            if (uriTemp != null) {
                uri = uriTemp
            }
        }
        Log.d("myLog", "MoYuFragment mUri is $uri")
        return uri
    }

    override fun onResume() {
        super.onResume()
        //Log.d("myLog", "onResume finalSlide is $finalSlide")
        //if (finalSlide != 0) {
        //moyu_appBarLayout.scrollTo(0, finalSlide)
        //moyu_coordinator_layout.scrollTo(0, finalSlide)
        //}
        val moyuBgPath = shp?.getString(MOYU_BG_PATH, "")
        //Log.d("myLog", "onResume moyuBgPath is $moyuBgPath")
        if (!moyuBgPath.equals("")) {
            Glide.with(this).load(moyuBgPath).into(binding.moyuBg)
        }

    }

    override fun onPause() {
        finalSlide = currentSlide
        //Log.d("myLog", "onPause  finalSlide is $finalSlide")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //Log.d("myLog", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        //Log.d("myLog", "onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
    }


}