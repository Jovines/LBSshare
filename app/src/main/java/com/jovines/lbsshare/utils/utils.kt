package com.jovines.lbsshare.utils

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.jovines.lbsshare.App
import com.jovines.lbsshare.App.Companion.context
import com.nanchen.compresshelper.CompressHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.DecimalFormat

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

fun MultipartBody.Builder.addImageToMultipartBodyBuilder(
    parameterName: String,
    imageUris: List<Uri>
) {
    imageUris.forEach {
        val fdd = CompressHelper.getDefault(context).compressToFile(File(getUriPath(it) ?: ""))
        val fileBody: RequestBody = fdd.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
        addFormDataPart(parameterName, fdd.name, fileBody)
    }
}

fun kmDecimalFormatString(number: Int): String {
    return when (number) {
        in 0L..999L -> DecimalFormat("0m").format(number)
        in 1000L..9999L -> DecimalFormat("0.#E0").format(number).replace(Regex("E.+"), "km")
        in 10000L..99999L -> DecimalFormat("00.#E0").format(number).replace(Regex("E.+"), "km")
        in 100000L..999999L -> DecimalFormat("000.#E0").format(number).replace(Regex("E.+"), "km")
        in 1000000L..9999999L -> DecimalFormat("0000.#E0").format(number)
            .replace(Regex("E.+"), "km")
        in 10000000L..99999999L -> DecimalFormat("00000.#E0").format(number)
            .replace(Regex("E.+"), "km")
        in 100000000L..999999999L -> DecimalFormat("000000.#E0").format(number)
            .replace(Regex("E.+"), "km")
        else -> ""
    }
}