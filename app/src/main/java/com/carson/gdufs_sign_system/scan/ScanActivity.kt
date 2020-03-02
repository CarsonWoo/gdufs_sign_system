package com.carson.gdufs_sign_system.scan

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.utils.PermissionUtils

class ScanActivity: BaseActivity() {

    private var mScanFragment: ScanFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        if (mScanFragment == null) {
            mScanFragment = ScanFragment.newInstance()
        }
        mScanFragment?.let {
            it.allowEnterTransitionOverlap = true
            it.allowReturnTransitionOverlap = true
            supportFragmentManager.beginTransaction()
                .add(R.id.scan_container, it, it.fragmentString()).commit()
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
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