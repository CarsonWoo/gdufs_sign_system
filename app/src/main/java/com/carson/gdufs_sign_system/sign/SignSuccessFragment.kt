package com.carson.gdufs_sign_system.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.utils.Const

class SignSuccessFragment: BaseFragment() {

    companion object {
        private const val FRAGMENT_TAG = "SignSuccess"
        fun newInstance() = SignSuccessFragment().apply {  }
    }

    private lateinit var mRoot: View
    private lateinit var mSignTime: TextView
    private lateinit var mBtnComplete: Button

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

        mSignTime.text = Const.getCurrentTime()
    }

    private fun initEvents() {
        mBtnComplete.setOnClickListener {
            (activity as SignActivity?)?.onBackPressed()
        }
    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

}