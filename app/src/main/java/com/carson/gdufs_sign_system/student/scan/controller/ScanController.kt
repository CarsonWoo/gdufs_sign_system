package com.carson.gdufs_sign_system.student.scan.controller

import android.content.Context
import android.graphics.Point
import android.hardware.camera2.CameraDevice
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import com.baidu.aip.face.MatchRequest
import com.baidu.aip.util.Base64Util
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.model.DetectFaceBean
import com.carson.gdufs_sign_system.student.scan.AipFaceObject
import com.carson.gdufs_sign_system.student.scan.IViewCallback
import com.carson.gdufs_sign_system.student.scan.ScanActivity
import com.carson.gdufs_sign_system.student.scan.ScanFragment
import com.carson.gdufs_sign_system.utils.*
import com.carson.gdufs_sign_system.widget.RoundTextureView
import com.google.gson.Gson
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ScanController(mFragment: ScanFragment, private val mIView: IViewCallback) :
    BaseController<ScanFragment>(mFragment),
    CameraListener, ImageSaveCallback {

    private lateinit var mTextureView: RoundTextureView
    private var mCameraHelper: CameraHelper? = null

    private var mIsTakingPhoto = false

    private var mFileName: String = ""

    private var mEnterType = Const.SCAN_ENTER_COMPARE

    companion object {
        private const val TAG = "ScanController"

        private const val CAMERA_ID = CameraHelper.CAMERA_ID_FRONT
    }

    fun onSubmitButtonClick(view: View, enterType: Int) {
        mEnterType = enterType
        Log.e(TAG, "mEnterType = $mEnterType")
        mFragment?.activity?.apply {
            mIsTakingPhoto = true
            mIView.onSwitchShadowText("提交数据中...")
            mCameraHelper?.takePhoto()
        }
    }

    fun setTextureView(textureView: RoundTextureView) {
        this.mTextureView = textureView
    }

    fun setResumePreview() {
        this.mIsTakingPhoto = false
        switchText("请将人脸放入取景框中", "请点击按钮拍照")
//        mIView.onSwitchText("请将人脸放入取景框中")
//        mIView.onSwitchShadowText("请点击拍照")
        // 先stop再start 重置一下参数
        mCameraHelper?.stop()
        mCameraHelper?.start()
    }

    fun onResume() {
        Log.e(TAG, "main currentThread = ${Thread.currentThread().name}")
        if (!mIsTakingPhoto) {
            mCameraHelper?.start()
        }
    }

    fun onPause() {
        if (!mIsTakingPhoto) {
            mCameraHelper?.stop()
        }
    }


    fun initCamera() {
        mTextureView ?: return

        Log.e(TAG, "initCamera")
        mCameraHelper = CameraHelper.Companion.Builder()
            .cameraListener(this)
            .specificCameraId(CAMERA_ID)
            .mContext(mFragment?.context!!)
            .previewOn(mTextureView)
            .previewViewSize(
                Point(
                    mTextureView.layoutParams.width,
                    mTextureView.layoutParams.height
                )
            )
            .rotation(mFragment?.activity?.windowManager?.defaultDisplay?.rotation ?: 0)
            .build()
        Log.e(TAG, "mCameraHelper = $mCameraHelper is null ? -> ${mCameraHelper == null}")
        mCameraHelper?.start()
        switchText("请将人脸放入取景框中", "请点击按钮拍照")
    }

    // 保存完capture的图片之后
    override fun onCompleted(bytes: ByteArray) {
        Log.e(TAG, "onSavingImageCompleted")

        Log.e(TAG, "saving complete currentThread = ${Thread.currentThread().name}")

        // 这里和ImageSaver还处于同一子线程中 无需再开启另一线程 但主线程的UI还是得用runOnUiThread
        mFragment?.activity?.runOnUiThread {
            switchText("检测人脸数据中...", "检测人脸中...")
        }

        val postImage = Base64Util.encode(bytes)

//        val path = mFragment?.activity?.application?.getExternalFilesDir(null)?.absolutePath +
//                File.separator + "signPhoto" + File.separator + "IMG_20200304_2120017.jpg"
//        Log.e(TAG, "path = $path")
//        val bmp = BitmapFactory.decodeFile(path)
//        val authBytes = bmp.byteCount
//        val buf = ByteBuffer.allocate(authBytes)
//        bmp.copyPixelsToBuffer(buf)
//        val authByteArray = buf.array()
//        Log.e(TAG, "authByteArray = ${authByteArray.size} and postByteArray = ${bytes.size}")

        val authImage = mFragment?.context?.getSharedPreferences("u1001", Context.MODE_PRIVATE)
            ?.getString("/storage/emulated/0/Android/data/com.carson.gdufs_sign_system/files/signPhoto/IMG_20200304_2152051.jpg", "")

        if (detectFace(postImage)) {
            // 检测成功
            if (mEnterType == Const.SCAN_ENTER_COMPARE) {
                // 进行比对
                if (matchFace(postImage, authImage)) {
                    // 比对成功
                    (mFragment?.activity as ScanActivity?)?.apply {
                        setResult(Const.RESULT_CODE_COMPARE_SUCCESS, null)
                        onBackPressed()
                    }
                }
            } else {
                // 进行提交
                switchText("提交数据中...", "")
                // todo post to server
                // 假设提交成功
            }
        }
    }

    // 保存图片失败
    override fun onFailure(msg: String?) {
        mFragment?.activity?.apply {
            runOnUiThread {
                setResumePreview()
                Toast.makeText(this, "截取的时候出现问题了哦~重拍一下吧~", Toast.LENGTH_SHORT).show()
            }
        }
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

    // 只有当capture完后会回调
    override fun onPreview(
        byteArray: ByteArray
    ) {
        Log.i(TAG, "onPreview: ")
        mFragment?.activity?.runOnUiThread {
//            mIView.onSwitchText("识别图片中...")
            switchText("识别图片中", "")
        }
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmsss", Locale.US)
        val fileName = "IMG_" + sdf.format(Date()) + ".jpg"
        val filePath = mFragment?.activity?.application?.getExternalFilesDir(null)?.absolutePath +
                File.separator + "signPhoto"

        val filePathDir = File(filePath)
        if (!filePathDir.exists()) {
            filePathDir.mkdirs()
        }

        mFragment?.context?.apply {
            val sp = getSharedPreferences("u1001", Context.MODE_PRIVATE)
            mFileName = filePath + File.separator + fileName
            sp.edit().putString(mFileName, Base64Util.encode(byteArray)).apply()
            Log.e(TAG, "fileName = $mFileName")

            LocalThreadPools.getInstance(this)
                .execute(ImageSaver(byteArray, mFileName, this@ScanController))

        }
//        val buf = ByteArray(byteBuffer.remaining())
//        val postImage = Base64Util.encode(buf)
//        val path = Environment.getExternalStorageDirectory().absolutePath + File.separator +

//        AipFaceObject.getClient().match()
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

    private fun switchText(content: String, shadowContent: String) {
        mFragment?.activity?.runOnUiThread {
            if (content.isNotEmpty()) {
                mIView.onSwitchText(content)
            }
            if (shadowContent.isNotEmpty()) {
                mIView.onSwitchShadowText(shadowContent)
            }
        }
    }

    private fun detectFace(postImage: String): Boolean {
        var bSuccess = false
        // 人脸检测
        val detectOptions = HashMap<String, String>()
        detectOptions["face_field"] = "age,gender,race,expression,beauty"
        detectOptions["face_type"] = "LIVE"
        val detectRes = AipFaceObject.getClient().detect(postImage, "BASE64", detectOptions)

        val mText: String
        val mShadowText: String

        if (detectRes.getInt("error_code") == 0) {
            // 检测成功
            bSuccess = true
            val detectBean = Gson().fromJson<DetectFaceBean>(detectRes.getJSONObject("result").toString(), DetectFaceBean::class.java)
            Log.e(TAG, "detect beauty=${detectBean.face_list[0].beauty} and expression=${detectBean.face_list[0].expression.type}")
            mText = "\n性别：${if (detectBean.face_list[0].gender.type == "male") "男" else "女"}" +
                    "\n年龄：${detectBean.face_list[0].age.toInt()}" +
                    "\n打分：${detectBean.face_list[0].beauty.toInt()}"
            mShadowText = "成功检测到人脸"
        } else {
            mText = "检测失败 请点击取景框重试"
            mShadowText = "检测失败"
        }

        mFragment?.activity?.runOnUiThread {
            switchText(mText, mShadowText)
//            mIView.onSwitchText(mText)
//            mIView.onSwitchShadowText(mShadowText)
        }
        return bSuccess
    }

    private fun matchFace(postImage: String?, authImage: String?): Boolean {
        var bSuccess = false

        if (postImage == null || authImage == null) {
            mFragment?.activity?.runOnUiThread {
                mIView.onSwitchText("出错了 请重试")
                mIView.onSwitchShadowText("点击这里重试")
            }
            return false
        }

        // 人脸比对
        val postMatchReq = MatchRequest(postImage, "BASE64")
        val authMathReq = MatchRequest(authImage, "BASE64")
        val res = AipFaceObject.getClient().match(listOf(postMatchReq, authMathReq))
        Log.e(TAG, "res = $res")

        val mText: String
        val mShadowText: String

        if (res.getInt("error_code") == 0) {
            // 比对成功
            val score = res.getJSONObject("result").getInt("score")
            Log.e(TAG, "score = $score")
            if (score > 50) {
                // match 比对结果得分大于50
                bSuccess = true
                mText = "人脸认证成功..正在跳转结果"
                mShadowText = "认证成功!"
            } else {
                mText = "比对不正确哦~请正对摄像头哦~"
                mShadowText = "点击这里重试"
            }
        } else {
            mText = "比对失败 请重试"
            mShadowText = "点击这里重试"
        }

        mFragment?.activity?.runOnUiThread {
            switchText(mText, mShadowText)
//            mIView.onSwitchShadowText(mShadowText)
//            mIView.onSwitchText(mText)
        }


        return bSuccess
    }

}