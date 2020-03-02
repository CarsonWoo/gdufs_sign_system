package com.carson.gdufs_sign_system.utils

import android.hardware.camera2.CameraDevice
import android.util.Size
import java.lang.Exception

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

    /**
     * 当相机关闭时执行
     */
    fun onCameraClosed()

    /**
     * 当出现异常
     */
    fun onCameraError(e: Exception)
}