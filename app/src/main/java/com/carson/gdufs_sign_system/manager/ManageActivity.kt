package com.carson.gdufs_sign_system.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.utils.StatusBarUtil

class ManageActivity : BaseActivity(), IViewCallback {

    private lateinit var mController: ManageController

    private lateinit var mLogoutButton: ImageView

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
        mLogoutButton = findViewById(R.id.btn_logout)
        mController = ManageController(this, this)
    }

    private fun initEvent() {
        mLogoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("确定退出登录么")
                .setPositiveButton("确定") {
                    dialog, _ ->
                    Log.i(TAG, "logout")
                    dialog.dismiss()
                }
                .setNegativeButton("取消") {
                    dialog, _ -> dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }
    }

    override fun onShowText(str: String, type: Int) {

    }

    override fun onDestroy() {
        mController.onDestroy()
        super.onDestroy()
    }
}
