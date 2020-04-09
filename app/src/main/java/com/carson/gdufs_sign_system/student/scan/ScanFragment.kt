package com.carson.gdufs_sign_system.student.scan

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.student.scan.controller.ScanController
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.widget.CircleTextureBorderView
import com.carson.gdufs_sign_system.widget.RoundTextureView
import kotlin.math.min

class ScanFragment : BaseFragment(), ViewTreeObserver.OnGlobalLayoutListener, IViewCallback {

    private lateinit var mRoot: View
    private lateinit var mBack: ImageView
    private lateinit var mBtnSubmit: Button
    private lateinit var mTextSwitcher: TextSwitcher
    private lateinit var mTextureView: RoundTextureView
    private lateinit var mTextureBorder: CircleTextureBorderView

    private var mController: ScanController? = null

    companion object {

        private const val TAG = "ScanFragment"

        private const val FRAGMENT_TAG = "Scan"

        fun newInstance(): ScanFragment {
            return ScanFragment().apply {

            }
        }
    }

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_scan, container, false)
        initViews()
        initEvents()
        return mRoot
    }

    private fun initViews() {
        mBack = mRoot.findViewById(R.id.scan_back)
        mTextureView = mRoot.findViewById(R.id.face_preview)
        mTextureBorder = mRoot.findViewById(R.id.face_border)
        mTextSwitcher = mRoot.findViewById(R.id.face_scan_text)
        mBtnSubmit = mRoot.findViewById(R.id.btn_submit)

        mTextSwitcher.setFactory {
            val textView = TextView(context)
            textView.textSize = 14F
            if ((activity as BaseActivity).isNightMode()) {
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setTextColor(Color.BLACK)
            }
            textView.gravity = Gravity.CENTER
            return@setFactory textView
        }

        mController = ScanController(this, this,
            arguments?.getLong(Const.BundleKeys.DETAIL_ID)?: 0L)
        Log.e(TAG, arguments?.getLong(Const.BundleKeys.DETAIL_ID)?.toString()?: 0L.toString())
        mController?.setTextureView(mTextureView)
    }

    private fun initEvents() {
        mTextureView.viewTreeObserver.addOnGlobalLayoutListener(this)
        mBack.setOnClickListener {
            (activity as ScanActivity?)?.onBackPressed()
        }
        mBtnSubmit.setOnClickListener {
            // 点击提交
            // click 的同时把可点击态取消
            mBtnSubmit.isEnabled = false
            Log.e(TAG, "onClickSubmitButton")
            mController?.onSubmitButtonClick(it, arguments?.getInt(Const.SCAN_ENTER_FLAG)
                ?: Const.SCAN_ENTER_COMPARE)
        }

        mTextureView.setOnClickListener {
            mBtnSubmit.isEnabled = true
            mController?.setResumePreview()
        }

        mTextSwitcher.setText("请将人脸放入取景框中")

        applyDarkMode()
    }

    private fun applyDarkMode() {
        (activity as BaseActivity).apply {
            if (isNightMode()) {
                mRoot.setBackgroundColor(resources.getColor(R.color.colorBlack))
            } else {
                mRoot.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
        }
    }

    override fun onSwitchText(text: String) {
        mTextSwitcher.setText(text)
    }

    override fun onSwitchShadowText(text: String) {
        mTextureBorder.setTipsText(text)
    }

    override fun onCaptureFailed() {
        mBtnSubmit.isEnabled = true
    }

    override fun onUploadSuccess() {
        (activity as ScanActivity?)?.onBackPressed()
    }

    override fun onGlobalLayout() {
        mTextureView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        val params = mTextureView.layoutParams
        val sideLength = min(mTextureView.width, mTextureView.height * 3 / 4)
        params.width = sideLength
        params.height = sideLength
        mTextureView.layoutParams = params
//        mTextureView.setRadius(min(mTextureView.width.toFloat(), mTextureView.height.toFloat()) / 2)
        Log.e("Texture", "sideLength = $sideLength")
        mTextureView.turnRound()
        mTextureBorder.setCircleTextureWidth(sideLength)
        if (PermissionUtils.isGranted(Manifest.permission.CAMERA, this.context!!)) {
            mController?.initCamera()
        } else {
            PermissionUtils.getInstance().with(this).permissions(Manifest.permission.CAMERA)
                .requestCode(PermissionUtils.CODE_CAMERA)
                .request(object : PermissionUtils.PermissionCallback {
                    override fun denied() {
                        PermissionUtils.getInstance().showDialog()
                    }

                    override fun granted() {
                        mController?.initCamera()
                    }
                })
        }

    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

    override fun onResume() {
        super.onResume()
        mController?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mController?.onPause()
    }

    override fun onDestroy() {
        mController?.onDestroy()
        super.onDestroy()
    }

}