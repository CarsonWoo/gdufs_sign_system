package com.carson.gdufs_sign_system.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.utils.StatusBarUtil

class ManageActivity : BaseActivity(), IViewCallback {

    private lateinit var mController: ManageController

    override fun getContentViewResId(): Int {
        return R.layout.activity_manage
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
        StatusBarUtil.setStatusBarDarkTheme(this, false)
        initView()
        initEvent()
    }

    private fun initView() {
        mController = ManageController(this, this)
    }

    private fun initEvent() {
    }

    override fun onShowText(str: String, type: Int) {

    }

    override fun onDestroy() {
        mController.onDestroy()
        super.onDestroy()
    }
}
