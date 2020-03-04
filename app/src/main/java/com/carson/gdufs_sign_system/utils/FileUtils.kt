package com.carson.gdufs_sign_system.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    private const val FILES_NAME = "/signPhoto"

    const val TIME_STYLE = "yyyyMMdd_HHmmss"

    const val IMAGE_TYPE = ".jpg"

    // 获取手机可存储路径
    private fun getPhoneRootPath(context: Context): String? {
        // 是否有SD卡
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
                !Environment.isExternalStorageRemovable()) {
            context.externalCacheDir.path
        } else {
            // 获取apk包下的缓存路径
            context.cacheDir.path
        }
    }

    fun getPhotoFileName(context: Context): String? {
        val file = File(getPhoneRootPath(context) + FILES_NAME)
        if (!file.exists()) {
            file.mkdirs()
        }
        val sdf = SimpleDateFormat(TIME_STYLE, Locale.US)
        val photoName = "/IMG_${sdf.format(Date())}$IMAGE_TYPE"
        Log.e("FileUtils", "photoName = $photoName")
        return file.path + photoName
    }

    fun savePhotoToSD(bitmap: Bitmap?, context: Context): String? {
        var outStream: FileOutputStream? = null
        val fileName = getPhotoFileName(context)
        try {
            outStream = FileOutputStream(fileName)
            // 数据写入文件
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            return fileName
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                outStream?.close()
                bitmap?.recycle()
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCompressPhoto(path: String, inSampleSize: Int): Bitmap {
        var options: BitmapFactory.Options? = BitmapFactory.Options()
        options?.inJustDecodeBounds = false
        options?.inSampleSize = inSampleSize
        val bmp = BitmapFactory.decodeFile(path, options)
        options = null
        return bmp
    }

    fun amendRotatePhoto(originPath: String, context: Context): String? {
        // 获取图片旋转角度
        val angle = readPictureDegree(originPath)

        // 原图压缩
        val bmp = getCompressPhoto(originPath, 1)

        // 修复图片被旋转的角度
        val bitmap = rotatingImageView(angle, bmp)

        // 保存修复后的图片并返回保存后的图片路径
        return savePhotoToSD(bitmap, context)
    }

    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    fun rotatingImageView(angle: Int, bitmap: Bitmap?): Bitmap? {
        var returnBmp: Bitmap? = null
        // 根据旋转角度 生成旋转矩阵
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        try {
            // 将原始图片按照旋转矩阵进行旋转 得到新的图片
            bitmap?.let {
                returnBmp = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
            }
        } catch (e: OutOfMemoryError) {

        }
        if (returnBmp == null) {
            returnBmp = bitmap
        }
        if (bitmap != returnBmp) {
            bitmap?.recycle()
        }
        return returnBmp
    }

}