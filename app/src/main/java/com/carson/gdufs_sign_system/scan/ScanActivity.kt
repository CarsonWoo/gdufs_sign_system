package com.carson.gdufs_sign_system.scan

import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity

class ScanActivity: BaseActivity() {

    private var mScanFragment: ScanFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_scan
    }

}