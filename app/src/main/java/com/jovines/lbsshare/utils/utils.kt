package com.jovines.lbsshare.utils

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.jovines.lbsshare.App
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream

/**
 * @author Jovines
 * @create 2020-04-28 6:03 PM
 *
 * description:
 *
 */

fun getUriPath(uri: Uri?): String? {
    var data: String? = null
    if (null == uri) return null
    val scheme: String? = uri.scheme
    if (scheme == null) data = uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
        data = uri.path
    } else if (ContentResolver.SCHEME_CONTENT == scheme) {
        val cursor: Cursor? = App.context.contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.ImageColumns.DATA),
            null,
            null,
            null
        )
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                }
            }
            cursor.close()
        }
    }
    return data
}

fun MultipartBody.Builder.addImageToMultipartBodyBuilder(parameterName: String, imageUris: List<Uri>) {
    imageUris.forEach {
        val fdd = File(BitmapUtil.compressImage(getUriPath(it)) ?: "")
        val pfd: ParcelFileDescriptor? = App.context.contentResolver.openFileDescriptor(it, "r")
        if (pfd != null) {
            val inputStream = FileInputStream(pfd.fileDescriptor)
            val fileBody: RequestBody =
                inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
            addFormDataPart(parameterName, fdd.name, fileBody)
        }
    }
}