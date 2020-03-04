package com.carson.gdufs_sign_system.utils

import android.hardware.camera2.CameraDevice
import android.media.Image
import android.util.Size
import java.lang.Exception
import java.nio.ByteBuffer

interface CameraListener {
    /**
     * 打开时执行
     *
     * @param cameraDevice    相机实例
     * @param cameraId        相机ID
     * @param isMirror        是否镜像显示
     */
    fun onCameraOpened(cameraDevice: CameraDevice, cameraId: String, previewSize: Size,
                       displayOrientation: Int, isMirror: Boolean)

//    /**
//     * 预览数据回调
//     *
//     * @param y            预览数据，Y分量
//     * @param u            预览数据，U分量
//     * @param v            预览数据，V分量
//     * @param previewSize  预览尺寸
//     * @param yRowStride   y步长
//     * @param uRowStride   u步长
//     * @param vRowStride   v步长
//     */
//    fun onPreview(y: ByteArray, u: ByteArray, v: ByteArray, previewSize: Size,
//                  yRowStride: Int, uRowStride: Int, vRowStride: Int)
    /**
     * 预览数据回调
     *
     * @param image     预览的Image对象
     */
    fun onPreview(byteArray: ByteArray)

    /**
     * 当相机关闭时执行
     */
    fun onCameraClosed()

    /**
     * 当出现异常
     */
    fun onCameraError(e: Exception)
}