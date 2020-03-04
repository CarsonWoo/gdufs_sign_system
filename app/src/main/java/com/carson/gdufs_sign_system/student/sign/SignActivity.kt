package com.carson.gdufs_sign_system.student.sign

import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.utils.PermissionUtils

class SignActivity : BaseFragmentActivity() {

    private var mMapFragment: MapFragment? = null
    private var mSignSuccessFragment: SignSuccessFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
    }

    override fun getContainerId(): Int {
        return R.id.sign_container
    }

    override fun getFragmentList(): MutableList<BaseFragment> {
        if (mMapFragment == null) {
            mMapFragment = MapFragment.newInstance()
        }
        if (mSignSuccessFragment == null) {
            mSignSuccessFragment = SignSuccessFragment.newInstance()
        }
        return mutableListOf(mMapFragment!!, mSignSuccessFragment!!)
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_sign
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        finish(SignActivity::class.java.name)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }

}