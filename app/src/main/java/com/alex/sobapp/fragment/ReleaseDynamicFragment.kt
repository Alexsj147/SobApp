package com.alex.sobapp.fragment

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.Html
import android.text.Html.ImageGetter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.alex.sobapp.R
import com.alex.sobapp.activity.ExchangeBgActivity
import com.alex.sobapp.adapter.ResultPicListAdapter
import com.alex.sobapp.apiService.EmojiList
import com.alex.sobapp.apiService.domain.DynamicBody
import com.alex.sobapp.apiService.domain.ImageItem
import com.alex.sobapp.base.BaseApplication
import com.alex.sobapp.base.BaseFragment
import com.alex.sobapp.custom.EmojiPop
import com.alex.sobapp.custom.LinkPop
import com.alex.sobapp.custom.PickerConfig
import com.alex.sobapp.custom.TopicPop
import com.alex.sobapp.databinding.FragmentReleaseDynamicBinding
import com.alex.sobapp.utils.Constants.MULTI_SELECT_COUNT
import com.alex.sobapp.utils.Constants.RECENT_EMOJI_LIST
import com.alex.sobapp.utils.Constants.TAG
import com.alex.sobapp.utils.LoadState
import com.alex.sobapp.viewmodel.EmojiViewModel
import com.alex.sobapp.viewmodel.ReleaseDynamicViewModel
import com.alex.sobapp.viewmodel.TopicViewModel
import com.alex.sobapp.viewmodel.UploadImageViewModel


class ReleaseDynamicFragment : BaseFragment<FragmentReleaseDynamicBinding>(),
    PickerConfig.OnImagesSelectedFinishedListener {

    private val shp = BaseApplication.getShp()
    private val shpEdit = BaseApplication.getShpEdit()

    private val resultPicListAdapter by lazy {
        ResultPicListAdapter()
    }

    private val mPickerConfig by lazy {
        PickerConfig.sPickerConfig
    }

    private val resultPicList = arrayListOf<ImageItem>()

    private val emojiPop by lazy {
        EmojiPop(context)
    }
    private val recentList = arrayListOf<Int>()

    private val emojiViewModel by lazy {
        EmojiViewModel()
    }

    private val topicPop by lazy {
        TopicPop(context)
    }

    private val topicViewModel by lazy {
        TopicViewModel()
    }

    private val uploadImageViewModel by lazy {
        UploadImageViewModel()
    }

    private val imageUrlList = arrayListOf<String>()
    private var mTopicId = ""
    private var mLinkUrl = ""
    private val linkPop by lazy {
        LinkPop(context)
    }

    private val releaseDynamicViewModel by lazy {
        ReleaseDynamicViewModel()
    }
    //override fun getPageLayoutId(): Int {
    //    return R.layout.fragment_release_dynamic
    //}

    override fun initEvent() {
        mPickerConfig.setMaxSelectedCount(MULTI_SELECT_COUNT)
        mPickerConfig.setOnImagesSelectedFinishedListener(this)
    }

    override fun initView() {
        switchUIByState(LoadState.SUCCESS)
        binding.dynamicInputBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    binding.releaseButton.setTextColor(resources.getColor(R.color.white))
                    binding.releaseButton.setBackgroundResource(R.drawable.release_button_bg_selector)
                } else {
                    binding.releaseButton.setTextColor(resources.getColor(R.color.ff999999))
                    binding.releaseButton.setBackgroundResource(R.drawable.release_button_bg_normal)
                }
            }
        })
        binding.dynamicPicListRv.run {
            layoutManager = LinearLayoutManager(rootView.context, HORIZONTAL, false)
            adapter = resultPicListAdapter
        }
    }

    override fun initObserver() {
        emojiViewModel.apply {
            recentEmojiList.observe(this@ReleaseDynamicFragment, Observer {
                //观察最近使用的emoji的变化
                //Log.d(TAG, "initObserver: recentList size is ${recentList.size}")
                //recentList.clear()
                //if (it.size != 0) {
                //} else {
                val list = arrayListOf<Int>()
                it.forEach { emojiId ->
                    //Log.d("myLog", "recentEmojiList Observer emojiId is $emojiId")
                    if (list.contains(emojiId)) {
                        list.remove(emojiId)
                    }
                    list.add(emojiId)
                }
                emojiPop.setRecentEmojiList(list)
                //}

            })
        }
        topicViewModel.apply {
            contentValue.observe(this@ReleaseDynamicFragment, Observer {
                //Log.d(TAG, "initObserver: $it")
                topicPop.setDataList(it)
            })
        }.loadTopicList()
        uploadImageViewModel.apply {
            contentValue.observe(this@ReleaseDynamicFragment, Observer {
                //得到返回的image的链接
                imageUrlList.add(it)
                Log.d(TAG, "imageUrlList is $imageUrlList")
            })
        }
        releaseDynamicViewModel.apply {
            isSucceed.observe(this@ReleaseDynamicFragment, Observer {
                Log.d(TAG, "发布动态：$it")
                if (it == true) {
                    findNavController().navigateUp()
                } else {
                    Toast.makeText(requireContext(), "发布动态失败", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun initListener() {
        binding.releaseBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.releaseButton.setOnClickListener {
            //发布动态
            val textContent = binding.dynamicInputBox.text.toString()
            val dynamicBody = DynamicBody(textContent, mLinkUrl, imageUrlList, mTopicId)
            Log.d("myLog", "发布动态：$dynamicBody")
            releaseDynamicViewModel.getReleaseResult(dynamicBody)
            //findNavController().navigateUp()
        }
        binding.selectPic.setOnClickListener {
            //选择图片
            pickImages(it)
        }
        binding.selectEmoji.setOnClickListener {
            //选择表情
            hideKeyboard(it)
            emojiPop.showAsDropDown(binding.selectEmoji)
            emojiPop.setOnAllEmojiClickListener(object : EmojiPop.OnAllEmojiClickListener {
                override fun onAllClick(emojiId: Int) {
                    //将点击过的表情添加到最近使用
                    //Log.d(TAG, "emojiId is $emojiId")
                    emojiViewModel.addRecentEmoji(emojiId)
                    val emojiIndex = EmojiList.emojiList.indexOf(emojiId)//下标
                    //Log.d(TAG, "emojiIndex is $emojiIndex")
                    val emojiPath = "https://cdn.sunofbeaches.com/emoji/${emojiIndex + 1}.png"
                    //Log.d(TAG, "emojiPath is $emojiPath")
                    //val drawable = ContextCompat.getDrawable(it.context, R.mipmap.f2)
                    binding.run {
                        //表情的大小和tv的文字大小一致,如果不设置就是宽高0/0
                        val textSize = dynamicInputBox.textSize.toInt()
                        //drawable?.setBounds(0, 0, textSize, textSize)
                        //val spannableString = Html.fromHtml(
                        //    dynamicInputBox.text.toString(),
                        //    ImageGetter { drawable },
                        //    null
                        //)
                        val imageGetter = object : ImageGetter {
                            override fun getDrawable(source: String?): Drawable {
                                val drawable = resources.getDrawable(emojiId)
                                drawable.setBounds(
                                    0,
                                    0,
                                    textSize,
                                    textSize
                                )
                                return drawable
                            }
                        }
                        val fromHtml = Html.fromHtml("<img src='$emojiPath'/>", imageGetter, null)
                        //dynamicInputBox.setText("${dynamicInputBox.text}$emojiPath")
                        dynamicInputBox.append(fromHtml)
                        dynamicInputBox.setSelection(dynamicInputBox.text.length)
                    }
                }

            })
        }
        binding.dynamicInputBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Log.d(TAG, "onTextChanged: textInfo is ${s.toString()}")
            }

        })
        binding.selectTopic.setOnClickListener {
            //选择话题
            hideKeyboard(it)
            topicPop.showAsDropDown(binding.selectTopic)
            topicPop.setOnTopicItemClickListener(object : TopicPop.OnTopicItemClickListener {
                override fun onTopicItemClick(topicId: String) {
                    //得到话题id
                    //Log.d(TAG, "得到话题id $topicId ")
                    mTopicId = topicId
                }
            })
        }
        binding.addLink.setOnClickListener {
            //添加链接
            hideKeyboard(it)
            linkPop.showAsDropDown(binding.addLink)
            linkPop.setOnAddLinkClickListener(object : LinkPop.OnAddLinkClickListener {
                override fun onClick(linkUrl: String) {
                    mLinkUrl = linkUrl
                }

            })
        }
    }

    private fun hideKeyboard(it: View) {
        //隐藏键盘
        val imm: InputMethodManager =
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }

    private fun pickImages(view: View) {
        val intent = Intent(view.context, ExchangeBgActivity::class.java)
        view.context.startActivity(intent)
    }

    override fun onSelectedImagesFinished(result: List<ImageItem>) {
        //选择的图片返回
        //result.forEach {
        //    Log.d("myLog", "返回的图片的path is  ${it.path}")
        //}
        resultPicList.clear()
        resultPicList.addAll(result)
        resultPicListAdapter.setData(resultPicList)
        //上传图片
        for (imageItem in result) {
            Log.d(TAG, "上传的图片:${imageItem.path} ")
            uploadImageViewModel.uploadImage(imageItem.path)
        }

    }

    override fun getBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentReleaseDynamicBinding {
        return FragmentReleaseDynamicBinding.inflate(inflater, viewGroup, false)
    }

    override fun onResume() {
        super.onResume()
        val recentEmojiList = shp?.getString(RECENT_EMOJI_LIST, "")
        Log.d(TAG, "onResume: recentEmojiList is $recentEmojiList")
        if (recentEmojiList != "null" && recentEmojiList != "") {
            val replaceStr = recentEmojiList?.replace("[", "")?.replace("]", "")?.replace(" ", "")
            //Log.d(TAG, "onResume: replaceStr is $replaceStr")
            val split = replaceStr?.split(",")
            //split?.forEach {
            //    //Log.d(TAG, "onResume: $it")
            //    recentList.add(it.toInt())
            //}
            Log.d(TAG, "onResume: split is ${split?.size}")
            split?.let {
                if (split.size < 5) {
                    for (element in split) {
                        recentList.add(element.toInt())
                    }
                } else {
                    for (i in 0..5) {
                        recentList.add(it[i].toInt())
                    }
                }
            }
            emojiPop.setRecentEmojiList(recentList)
            emojiViewModel.setRecentEmoji(recentList)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val recentEmoji = emojiViewModel.recentEmojiList.value
        //Log.d(TAG, "onDestroyView: recentEmoji is $recentEmoji")
        if (recentEmoji != null) {
            val recentList = shp?.getString(RECENT_EMOJI_LIST, "")
            if (recentList.equals(recentEmoji.toString())) {
                //
            } else {
                shpEdit?.putString(RECENT_EMOJI_LIST, recentEmoji.toString())
                shpEdit?.apply()
            }
        }
    }


}

