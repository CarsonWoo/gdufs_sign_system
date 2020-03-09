package com.carson.gdufs_sign_system.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity

class ManageActivity : BaseActivity(), IViewCallback {

    private lateinit var mTv: TextView
    private lateinit var mBtn: Button

    private lateinit var mController: ManageController

    override fun getContentViewResId(): Int {
        return R.layout.activity_manage
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        initView()
        initEvent()
    }

    private fun initView() {
        mTv = findViewById(R.id.tv_test)
        mBtn = findViewById(R.id.btn_test)
        mController = ManageController(this, this)
    }

    private fun initEvent() {
        mBtn.setOnClickListener {
            mController.requestNetwork()
        }
    }

    override fun onShowText(str: String, type: Int) {
        if (type == 0) {
            mTv.text = str
        } else {
            mTv.append(str)
        }
    }

    override fun onDestroy() {
        mController.onDestroy()
        super.onDestroy()
    }
}
