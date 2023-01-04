package com.alex.sobapp.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.alex.sobapp.R
import com.alex.sobapp.apiService.domain.ImageItem
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.custom.PickerConfig
import com.alex.sobapp.databinding.FragmentExchangeBgBinding
import com.alex.sobapp.utils.Constants
import com.alex.sobapp.utils.Constants.MOYU_BG_PATH
import com.alex.sobapp.utils.CreateIntent
import com.alex.sobapp.utils.LoadState
import com.bumptech.glide.Glide
import java.io.FileNotFoundException
import java.io.IOException

class ExchangeBgFragment : BaseFragment<FragmentExchangeBgBinding>() {

    private val pickerConfig by lazy {
        PickerConfig.sPickerConfig
    }
    private var mTempFile: Uri? = null
    private val shp = BaseApplication.getShp()
    private val shpEdit = BaseApplication.getShpEdit()

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentExchangeBgBinding {
        return FragmentExchangeBgBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        //pickerConfig.setMaxSelectedCount(Constants.SINGLE_SELECT_COUNT)
        //pickerConfig.setOnSingleImageFinishedListener(this)
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
    }

    override fun initListener() {
        super.initListener()
        binding.exchangeBack.setOnClickListener {
            activity?.finish()
        }
        binding.selectFromAlbum.setOnClickListener {
            //获取相册内容
            //startActivity(Intent(it.context, PickerActivity::class.java))
            //findNavController().navigate(R.id.toPickerFragment)
            val imageIntent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            albumLauncher.launch(imageIntent)
        }
        binding.selectFromCamera.setOnClickListener {
            //调用摄像头
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(intent)
        }
    }

    /**
     * 打开相册,选择图片
     */
    private val albumLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activity_result ->
            if (activity_result.resultCode == Activity.RESULT_OK) {
                val uri = activity_result.data?.data
                //Log.d("myLog", "data is $uri")
                var bitmap: Bitmap? = null
                var str: String? = ""
                //Log.d("myLog", "uri is $uri")
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
                //Log.d("myLog", "parseUri is $parseUri")
                //---------------------------------------//
                //val cropIntent = createIntent(parseUri)
                val cropIntent = CreateIntent.create(parseUri)
                //调用系统裁切
                albumCropLauncher.launch(cropIntent)
            }
        }

    /**
     * 调用相机
     */
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                //说明成功咯
                //println("result is $activityResult.data")
                val intentData = activityResult.data
                val bitmap = (intentData?.extras?.get("data")) as Bitmap
                val bitmapStr =
                    MediaStore.Images.Media.insertImage(activity?.contentResolver, bitmap, "", "")
                val parseUri = Uri.parse(bitmapStr)
                //Log.d("myLog", "parseUri is $parseUri")
                //val cropIntent = createIntent(parseUri)
                val cropIntent = CreateIntent.create(parseUri)
                albumCropLauncher.launch(cropIntent)
            }
        }
    /**
     * 调用系统的裁切
     */
    private val albumCropLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            //Log.d("myLog", "binding is $binding")
            //Log.d("myLog", "resultCode is ${activityResult.resultCode}")
            if (activityResult.resultCode == Activity.RESULT_OK) {
                //Log.d("myLog", "brand is ${Build.BRAND}")
                if (Build.BRAND == "Xiaomi") {
                    try {
                        val bitmap = BitmapFactory.decodeStream(
                            activity?.contentResolver?.openInputStream(mTempFile!!)
                        )
                        //Glide.with(this).load(bitmap).into(binding.moyuBg)
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
                    activity?.finish()
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
}