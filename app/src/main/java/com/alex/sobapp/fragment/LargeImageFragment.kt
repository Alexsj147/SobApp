package com.alex.sobapp.fragment

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.alex.sobapp.adapter.LargeImageAdapter
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.databinding.FragmentLargeImageBinding
import com.alex.sobapp.utils.LoadState
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LargeImageFragment : BaseFragment<FragmentLargeImageBinding>() {

    val REQUEST_CODE = 100
    private var imageListString: String = ""
    private var currentPosition: Int = 0
    private val imageList = arrayListOf<String>()
    private val largeImageAdapter by lazy {
        LargeImageAdapter()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentLargeImageBinding {
        return FragmentLargeImageBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()
        //设置状态栏字体颜色
        ImmersionBar.with(this).statusBarDarkFont(false).init()
        arguments?.let {
            imageListString = it.getString("imageList", "")
            currentPosition = it.getInt("currentPosition")
        }
        //Log.d("myLog","imageListString is $imageListString")
    }

    override fun initView() {
        super.initView()
        switchUIByState(LoadState.SUCCESS)
        binding.largeViewPager.apply {
            adapter = largeImageAdapter
        }
        val imageListStr = imageListString.split(",")
        for (image in imageListStr) {
            imageList.add(image)
        }
        largeImageAdapter.setData(imageList)
        binding.largeViewPager.currentItem = currentPosition
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        binding.largeImageBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.saveImageButton.setOnClickListener {
            //保存图片
            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    //保存图片
                    savePhoto()
                }
            }
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewLifecycleOwner.lifecycleScope.launch {
                    //保存图片
                    savePhoto()
                }
            } else {
                Toast.makeText(requireContext(), "保存失败", Toast.LENGTH_SHORT).show()
            }
        }

    private suspend fun savePhoto() {
        withContext(Dispatchers.IO) {
            val holder =
                (binding.largeViewPager[0] as RecyclerView).findViewHolderForAdapterPosition(
                    binding.largeViewPager.currentItem
                ) as LargeImageAdapter.InnerHolder
            val bitmap = holder.itemLargeImageBinding.photoView.drawable.toBitmap()
            //sdk>29被弃用
            //if (MediaStore.Images.Media.insertImage(
            //        requireContext().contentResolver,
            //        bitmap,
            //        "",
            //        ""
            //    ) == null
            //) {
            //    Toast.makeText(requireContext(), "保存失败", Toast.LENGTH_SHORT).show()
            //} else {
            //    Toast.makeText(requireContext(), "保存成功", Toast.LENGTH_SHORT).show()
            //}
            val saveUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            ) ?: kotlin.run {
                Toast.makeText(requireContext(), "保存失败", Toast.LENGTH_SHORT).show()
                return@withContext
            }
            requireContext().contentResolver.openOutputStream(saveUri).use {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)) {
                    MainScope().launch {
                        Toast.makeText(requireContext(), "保存成功", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    MainScope().launch {
                        Toast.makeText(requireContext(), "保存失败", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}