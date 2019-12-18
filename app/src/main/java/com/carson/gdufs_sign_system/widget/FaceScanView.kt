package com.carson.gdufs_sign_system.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.carson.gdufs_sign_system.R

class FaceScanView: ConstraintLayout {

    private lateinit var mRoot: View
    private lateinit var mDetectView: FrameLayout
    private lateinit var mDetectTextView: TextView
    private lateinit var mAvatar: CircleImageView

    private lateinit var mHandler: Handler

    constructor(context: Context?): this(context, null)

    constructor(context: Context?, attributeSet: AttributeSet?): this(context, attributeSet, 0)

    constructor(context: Context?, attributeSet: AttributeSet?, defStyle: Int): super(context, attributeSet, defStyle) {
        initViews(context)
    }

    private fun initViews(context: Context?) {
        mRoot = LayoutInflater.from(context).inflate(R.layout.face_scan_view, this, true)
        mDetectView = mRoot.findViewById(R.id.detect_view)
        mDetectTextView = mRoot.findViewById(R.id.detect_text)
        mAvatar = mRoot.findViewById(R.id.avatar)

        mHandler = object: Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
            }
        }
    }


}