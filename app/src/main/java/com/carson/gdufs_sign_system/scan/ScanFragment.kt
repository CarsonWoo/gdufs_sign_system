package com.carson.gdufs_sign_system.scan

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.scan.controller.ScanController
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.widget.CircleImageView
import com.carson.gdufs_sign_system.widget.RoundTextureView
import kotlin.math.min

class ScanFragment : BaseFragment(), ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var mRoot: View
    private lateinit var mBack: ImageView
    private lateinit var mBtnSubmit: Button
    private lateinit var mTextSwitcher: TextSwitcher
    private lateinit var mTextureView: RoundTextureView

    private var mController: ScanController? = null

    companion object {

        private const val TAG = "ScanFragment"

        private const val FRAGMENT_TAG = "Scan"

        fun newInstance() = ScanFragment().apply {

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
        mTextSwitcher = mRoot.findViewById(R.id.face_scan_text)
        mBtnSubmit = mRoot.findViewById(R.id.btn_submit)

        mTextSwitcher.setFactory {
            val textView = TextView(context!!)
            textView.textSize = 14F
            textView.setTextColor(Color.BLACK)
            textView.gravity = Gravity.CENTER
            return@setFactory textView
        }

        Log.i(TAG, "initViews()")
        mController = ScanController(this)
        mController?.setTextureView(mTextureView)

    }

    private fun initEvents() {
        mTextureView.viewTreeObserver.addOnGlobalLayoutListener(this)
        mBack.setOnClickListener {
            (activity as ScanActivity?)?.onBackPressed()
        }
        mBtnSubmit.setOnClickListener {
            // 点击提交
            mTextSwitcher.setText("检测到人脸")
            mController?.onSubmitButtonClick(it)
        }

        mTextSwitcher.setText("请将人脸放入取景框中")
    }

    override fun onGlobalLayout() {
        mTextureView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        val params = mTextureView.layoutParams
        val sideLength = min(mTextureView.width, mTextureView.height * 3 / 4)
        params.width = sideLength
        params.height = sideLength
        mTextureView.layoutParams = params
//        mTextureView.setRadius(min(mTextureView.width.toFloat(), mTextureView.height.toFloat()) / 2)
        mTextureView.turnRound()
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