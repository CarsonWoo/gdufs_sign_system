package com.carson.gdufs_sign_system.student.sign

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.utils.Const

class SignSuccessFragment : BaseFragment() {

    companion object {
        private const val FRAGMENT_TAG = "SignSuccess"
        fun newInstance() = SignSuccessFragment().apply { }
    }

    private lateinit var mRoot: View
    private lateinit var mSignTime: TextView
    private lateinit var mBtnComplete: Button
    private lateinit var mSuccessDetail: LinearLayout

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_sign_success, container, false)
        initViews()
        initEvents()
        return mRoot
    }

    private fun initViews() {
        mSignTime = mRoot.findViewById(R.id.sign_time)
        mBtnComplete = mRoot.findViewById(R.id.btn_complete)
        mSuccessDetail = mRoot.findViewById(R.id.success_detail)

        mSignTime.text = Const.getCurrentTime()
    }

    fun setCurrentTime() {
        mSignTime.text = Const.getCurrentTime()
    }

    private fun initEvents() {
        applyDarkMode()
        mBtnComplete.setOnClickListener {
            (activity as SignActivity?)?.apply {
                setResult(Const.RESULT_CODE_SIGN_SUCCESS)
                onBackPressed()
            }
        }
    }

    private fun applyDarkMode() {
        for (v in mSuccessDetail.children) {
            if (v is TextView) {
                if ((activity as BaseActivity).isNightMode()) {
                    v.setTextColor(Color.WHITE)
                } else {
                    v.setTextColor(Color.BLACK)
                }
            }
        }

    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

}