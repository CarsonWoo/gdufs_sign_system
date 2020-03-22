package com.carson.gdufs_sign_system.manager.manage

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.login.LoginActivity
import com.carson.gdufs_sign_system.manager.lookup.LookupActivity
import com.carson.gdufs_sign_system.manager.post.PostActivity
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import java.lang.ref.WeakReference

class ManageActivity : BaseActivity(), IViewCallback {

    private lateinit var mController: ManageController

    private lateinit var mLogoutButton: ImageView

    private lateinit var mPostSign: RelativeLayout

    private lateinit var mLookupSign: RelativeLayout

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
        mPostSign = findViewById(R.id.manage_post_sign)
        mLookupSign = findViewById(R.id.manage_lookup_sign)
        mController = ManageController(this, this)
    }

    private fun initEvent() {
        mLogoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("确定退出登录么")
                .setPositiveButton("确定") {
                    dialog, _ ->
                    doLogout()
                    dialog.dismiss()
                }
                .setNegativeButton("取消") {
                    dialog, _ -> dialog.dismiss()
                }
                .setCancelable(true)
                .show()
        }
        mPostSign.setOnClickListener {
            Intent(this, PostActivity::class.java).apply {
                startActivity(this)
                overridePendingTransition(R.anim.slide_right_in, R.anim.scale_out)
            }
        }
        mLookupSign.setOnClickListener {
            Intent(this, LookupActivity::class.java).apply {
                startActivity(this)
                overridePendingTransition(R.anim.slide_right_in, R.anim.scale_out)
            }
        }
    }

    private fun doLogout() {
        Const.getSharedPreference(WeakReference(this))?.edit()?.clear()?.apply()
        Intent(this, LoginActivity::class.java)
            .apply {
                startActivity(this)
                finish(ManageActivity::class.java.name)
            }
    }

    override fun onShowText(str: String, type: Int) {

    }

    override fun onDestroy() {
        mController.onDestroy()
        super.onDestroy()
    }
}
