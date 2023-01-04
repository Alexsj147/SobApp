package com.alex.sobapp.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore

class CreateIntent {
    companion object{
        private var mTempFile: Uri? = null
        fun create(parseUri: Uri?):Intent{
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
    }
}