package com.carson.gdufs_sign_system.utils

import android.media.Image
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

class ImageSaver(byteArray: ByteArray, filePath: String,
                 private val mCallback: ImageSaveCallback?): Runnable {

    private var mByteArray: ByteArray = byteArray
    private val mFilePath= filePath
    private var mFile: File? = null

    override fun run() {
        Log.e("ImageSaver", "image saver currentThread = ${Thread.currentThread().name}")
        var output: FileOutputStream? = null
        mFile = File(mFilePath)

        try {
//            Log.e("ImageSaver", "bytes.size = ${bytes.size}")
//            mBuffer.get(bytes)
            output = FileOutputStream(mFile)
            output.write(mByteArray)
//            Log.e("ImageSaver", "bytes.size = ${bytes.size}")
            mCallback?.onCompleted(mByteArray)
        } catch (e: Exception) {
            e.printStackTrace()
            mCallback?.onFailure(e.message)
        } finally {
            try {
                output?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

}

interface ImageSaveCallback {
    fun onCompleted(bytes: ByteArray)

    fun onFailure(msg: String?)
}