package com.carson.gdufs_sign_system.scan.controller

import android.Manifest
import android.graphics.Point
import android.hardware.camera2.CameraDevice
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.scan.AipFaceObject
import com.carson.gdufs_sign_system.scan.ScanActivity
import com.carson.gdufs_sign_system.scan.ScanFragment
import com.carson.gdufs_sign_system.utils.CameraHelper
import com.carson.gdufs_sign_system.utils.CameraListener
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.widget.RoundTextureView
import java.lang.Exception
import kotlin.math.min

class ScanController(mFragment: ScanFragment): BaseController<ScanFragment>(mFragment),
    CameraListener {

    private lateinit var mTextureView: RoundTextureView
    private var mCameraHelper: CameraHelper? = null

    companion object {
        private const val TAG = "ScanController"

        private const val CAMERA_ID = CameraHelper.CAMERA_ID_FRONT
    }

    fun onSubmitButtonClick(view: View) {

    }

    fun setTextureView(textureView: RoundTextureView) {
        this.mTextureView = textureView
    }

    fun onResume() {
        mCameraHelper?.start()
    }

    fun onPause() {
        mCameraHelper?.stop()
    }


    fun initCamera() {
        mTextureView?: return

        Log.e(TAG, "initCamera")
        mCameraHelper = CameraHelper.Companion.Builder()
            .cameraListener(this)
            .specificCameraId(CAMERA_ID)
            .mContext(mFragment?.context!!)
            .previewOn(mTextureView)
            .previewViewSize(Point(mTextureView.layoutParams.width, mTextureView.layoutParams.height))
            .rotation(mFragment?.activity?.windowManager?.defaultDisplay?.rotation?: 0)
            .build()
        Log.e(TAG, "mCameraHelper = $mCameraHelper is null ? -> ${mCameraHelper == null}")
        mCameraHelper?.start()
    }

    override fun onCameraOpened(
        cameraDevice: CameraDevice,
        cameraId: String,
        previewSize: Size,
        displayOrientation: Int,
        isMirror: Boolean
    ) {
        Log.i(TAG, "onCameraOpened:  previewSize = ${previewSize.width}  x  ${previewSize.height}")
        // 相机打开时，添加右上角的view用于显示原始数据和预览数据
        (mFragment?.activity as ScanActivity?)?.apply {
            runOnUiThread {
                // 将预览控件和预览尺寸比例保持一致 避免拉伸
                val params = mTextureView.layoutParams
                // 横屏
                if (displayOrientation % 180 == 0) {
                    params.height = params.width * previewSize.height / previewSize.width
                }
                // 竖屏
                else {
                    params.height = params.width * previewSize.width / previewSize.height
                }
                mTextureView.layoutParams = params
            }
        }
    }

    override fun onCameraClosed() {
    }

    override fun onCameraError(e: Exception) {
    }

    override fun onDestroy() {
        mCameraHelper?.release()
        PermissionUtils.getInstance().destroy()
        super.onDestroy()
    }

}