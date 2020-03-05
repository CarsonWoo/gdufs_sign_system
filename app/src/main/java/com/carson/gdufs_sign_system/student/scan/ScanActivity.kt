package com.carson.gdufs_sign_system.student.scan

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.StatusBarUtil

class ScanActivity: BaseActivity() {

    private var mScanFragment: ScanFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        if (mScanFragment == null) {
            var enterFlag = this.intent.extras?.getInt(Const.SCAN_ENTER_FLAG, Const.SCAN_ENTER_COMPARE)
            if (enterFlag == null) enterFlag = Const.SCAN_ENTER_COMPARE
            mScanFragment = ScanFragment.newInstance(enterFlag)
        }
        mScanFragment?.let {
            it.allowEnterTransitionOverlap = true
            it.allowReturnTransitionOverlap = true
            supportFragmentManager.beginTransaction()
                .add(R.id.scan_container, it, it.fragmentString()).commit()
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val params = findViewById<FrameLayout>(R.id.scan_container).layoutParams as FrameLayout.LayoutParams
        params.topMargin = StatusBarUtil.getStatusBarHeight(this)
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
        StatusBarUtil.setStatusBarDarkTheme(this, false)
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_scan
    }

    override fun onBackPressed() {
        finish(ScanActivity::class.java.name)
        overridePendingTransition(R.anim.scale_in, R.anim.slide_right_out)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.getInstance().with(this).onRequestPermissionResult(requestCode, permissions, grantResults)
    }

}