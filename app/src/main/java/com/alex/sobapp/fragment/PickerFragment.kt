package com.alex.sobapp.fragment

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.alex.sobapp.R
import com.alex.sobapp.adapter.PickerAdapter
import com.alex.sobapp.apiService.domain.ImageItem
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.custom.PickerConfig
import com.alex.sobapp.databinding.FragmentPickerBinding
import com.alex.sobapp.utils.Constants.MULTI_SELECT_COUNT
import com.alex.sobapp.utils.Constants.SINGLE_SELECT_COUNT
import com.alex.sobapp.utils.LoadState


class PickerFragment : BaseFragment<FragmentPickerBinding>(),
    PickerAdapter.OnItemSelectedChangeListener,
    PickerAdapter.OnItemClickListener {

    private val mImageItems = arrayListOf<ImageItem>()
    private val pickerAdapter by lazy {
        PickerAdapter()
    }

    private var maxSelectedCount: Int = 0

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentPickerBinding {
        return FragmentPickerBinding.inflate(inflater, viewGroup, false)
    }

    override fun initEvent() {
        super.initEvent()

        //firstTryToGetPic()
        //initView()
        initConfig()
        //initListener()
    }

    private val mPickerConfig by lazy {
        PickerConfig.sPickerConfig
    }

    private fun initConfig() {
        maxSelectedCount = mPickerConfig.getMaxSelectedCount()
        pickerAdapter.setMaxSelectedCount(maxSelectedCount)
    }


    override fun initView() {
        super.initView()
        initLoadedManager()
        switchUIByState(LoadState.SUCCESS)
        binding.pickerImageList.run {
            layoutManager = GridLayoutManager(binding.root.context, 3)
            adapter = pickerAdapter
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        binding.pickerCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        pickerAdapter.setOnItemSelectedChangeListener(this)
        pickerAdapter.setOnItemClickListener(this)
        binding.pickerFinish.setOnClickListener {
            val selectedItem = pickerAdapter.getSelectedItem()
            //Log.d("myLog", "返回的selectedItem is  $selectedItem")
            if (maxSelectedCount == SINGLE_SELECT_COUNT) {
                val singleImageFinishedListener = mPickerConfig.getSingleImageFinishedListener()
                //Log.d("myLog", "listener is  $singleImageFinishedListener")
                singleImageFinishedListener?.onSingleImageFinished(selectedItem)
            }
            if (maxSelectedCount == MULTI_SELECT_COUNT) {
                val imagesSelectedFinishedListener =
                    mPickerConfig.getImagesSelectedFinishedListener()
                imagesSelectedFinishedListener?.onSelectedImagesFinished(selectedItem)
            }
            pickerAdapter.release()
            activity?.finish()
        }
    }

    override fun onItemSelectedChange(selectedItem: List<ImageItem>) {
        //所选择的数据发生变化
        //Log.d("myLog", "selectedItem size is ${selectedItem.size}")
        if (selectedItem.isNotEmpty()) {
            binding.pickerFinish.background =
                resources.getDrawable(R.drawable.picker_finish_button_selector_bg)
            binding.pickerFinish.setTextColor(resources.getColor(R.color.white))
            binding.pickerFinish.text =
                "(" + selectedItem.size + "/" + pickerAdapter.getMaxSelectedCount() + ")完成"
        } else {
            binding.pickerFinish.background =
                resources.getDrawable(R.drawable.picker_finish_button_normal_bg)
            binding.pickerFinish.setTextColor(resources.getColor(R.color.ffbfbfbf))
            binding.pickerFinish.text = "完成"
        }
    }

    override fun onItemClickListener(imageItem: ImageItem) {
        //查看大图
        val bundle = Bundle()
        bundle.putString("photoPath", imageItem.path)
        findNavController().navigate(R.id.toPhotoFragment, bundle)
    }


    private fun initLoadedManager() {
        mImageItems.clear()
        val loaderManager = LoaderManager.getInstance(this)
        loaderManager.initLoader(1, null, object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                if (id == 1) {
                    return CursorLoader(
                        binding.root.context,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        arrayOf("_data", "_display_name", "date_added"),
                        null,
                        null,
                        "date_added DESC"
                    )
                } else null!!
            }

            override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
                if (cursor != null) {
                    if (cursor.isClosed) {
                        return
                    }
                    while (cursor.moveToNext()) {
                        val path: String = cursor.getString(0)
                        val title: String = cursor.getString(1)
                        val date: Long = cursor.getLong(2)
                        val imageItem = ImageItem(path, title, date)
                        //println("imageItem is $imageItem")
                        mImageItems.add(imageItem)
                    }
                    cursor.close()
                    //                    for (ImageItem imageItem : mImageItems) {
//                        Log.d(TAG,"imageItem == >"+imageItem);
//                    }
                    pickerAdapter.setData(mImageItems)
                }
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {

            }

        })
    }

    private fun firstTryToGetPic() {
        val contentResolver = activity?.contentResolver
        val uri = MediaStore.Files.getContentUri("external")
        val query = contentResolver?.query(uri, null, null, null, null)
        val columnNames = query!!.columnNames
        while (query.moveToNext()) {
            Log.d("myLog", "============================")
            for (columnName in columnNames) {
                Log.d(
                    "myLog", columnName + "-----" + query.getColumnIndex(columnName)
                )
            }
            Log.d("myLog", "============================")
        }
        query.close()

    }

}