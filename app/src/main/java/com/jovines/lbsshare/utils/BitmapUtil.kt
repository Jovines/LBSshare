package com.jovines.lbsshare.utils

import android.graphics.*
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt


/**
 * @author jon
 * @create 2020-05-08 4:21 PM
 *
 * 描述:
 *
 */
object BitmapUtil {
    private const val PHOTO_FILE_NAME = "PMSManagerPhoto"

    /**
     * 获取图片的旋转角度
     *
     * @param filePath
     * @return
     */
    fun getRotateAngle(filePath: String): Int {
        var rotateAngle = 0
        try {
            val exifInterface = ExifInterface(filePath)
            val orientation: Int = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateAngle = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateAngle = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateAngle = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotateAngle
    }

    /**
     * 旋转图片角度
     *
     * @param angle
     * @param bitmap
     * @return
     */
    fun setRotateAngle(angle: Int, bitmap: Bitmap?): Bitmap? {
        var mBitmap = bitmap
        if (mBitmap != null) {
            val m = Matrix()
            m.postRotate(angle.toFloat())
            mBitmap = Bitmap.createBitmap(
                mBitmap, 0, 0, mBitmap.width,
                mBitmap.height, m, true
            )
            return mBitmap
        }
        return mBitmap
    }

    //转换为圆形状的bitmap
    fun createCircleImage(source: Bitmap): Bitmap {
        val length =
            if (source.width < source.height) source.width else source.height
        val paint = Paint()
        paint.setAntiAlias(true)
        val target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target)
        canvas.drawCircle(length / 2f, length / 2f, length / 2f, paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(source, 0f, 0f, paint)
        return target
    }

    /**
     * 图片压缩-质量压缩
     *
     * @param filePath 源图片路径
     * @return 压缩后的路径
     */
    fun compressImage(filePath: String?): String? {
        filePath?:return null
        //原文件
        val oldFile = File(filePath)

        //压缩文件路径 照片路径/
        val targetPath: String = oldFile.path
        val quality = 50 //压缩比例0-100
        var bm: Bitmap? = getSmallBitmap(filePath) //获取一定尺寸的图片
        val degree = getRotateAngle(filePath) //获取相片拍摄角度
        if (degree != 0) { //旋转照片角度，防止头像横着显示
            bm = setRotateAngle(degree, bm)
        }
        val outputFile = File(targetPath)
        try {
            if (!outputFile.exists()) {
                outputFile.parentFile?.mkdirs()
                //outputFile.createNewFile();
            } else {
                outputFile.delete()
            }
            val out = FileOutputStream(outputFile)
            bm!!.compress(Bitmap.CompressFormat.JPEG, quality, out)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return filePath
        }
        return outputFile.path
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    fun getSmallBitmap(filePath: String?): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true //只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options)
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800)
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int, reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio =
                (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio =
                (width.toFloat() / reqWidth.toFloat()).roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }
}