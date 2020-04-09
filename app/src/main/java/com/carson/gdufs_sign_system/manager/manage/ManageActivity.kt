package com.carson.gdufs_sign_system.manager.manage

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.login.LoginActivity
import com.carson.gdufs_sign_system.manager.lookup.LookupActivity
import com.carson.gdufs_sign_system.manager.post.PostActivity
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.carson.gdufs_sign_system.widget.RoundImageView
import com.carson.gdufs_sign_system.widget.TipsDialog
import java.lang.ref.WeakReference

class ManageActivity : BaseActivity(), IViewCallback, TipsDialog.OnTipsDialogClickListener {

    private lateinit var mController: ManageController

    private lateinit var mLogoutButton: ImageView

    private lateinit var mPostSign: RelativeLayout

    private lateinit var mLookupSign: RelativeLayout

    private lateinit var mAvatar: RoundImageView

    private lateinit var mUsername: TextView

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
        mAvatar = findViewById(R.id.manager_avatar)
        mUsername = findViewById(R.id.manager_account)

        mUsername.text = Const.getSharedPreference(WeakReference(this))?.getString(Const.PreferenceKeys.USER_ID, "")
        Glide.with(this).load(R.drawable.icon_manager).into(mAvatar)

        mController = ManageController(WeakReference(this), this)
    }

    private fun initEvent() {
        mLogoutButton.setOnClickListener {
            TipsDialog(WeakReference(this)).apply {
                setTips("确定退出登录么")
                setListener(this@ManageActivity)
                show()
            }
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

    override fun onConfirm(dialog: TipsDialog) {
        dialog.dismiss()
        doLogout()
    }

    override fun onCancel(dialog: TipsDialog) {
        dialog.dismiss()
    }

    override fun onDestroy() {
        mController.onDestroy()
        super.onDestroy()
    }
}
