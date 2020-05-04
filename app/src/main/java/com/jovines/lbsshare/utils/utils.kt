package com.jovines.lbsshare.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.jovines.lbsshare.App

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