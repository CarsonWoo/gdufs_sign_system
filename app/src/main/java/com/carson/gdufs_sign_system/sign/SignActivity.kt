package com.carson.gdufs_sign_system.sign

import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity

class SignActivity : BaseFragmentActivity() {

    private var mMapFragment: MapFragment? = null
    private var mSignSuccessFragment: SignSuccessFragment? = null

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

    override fun onBackPressed() {
        finish(SignActivity::class.java.name)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }

}