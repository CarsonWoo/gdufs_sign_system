package com.carson.gdufs_sign_system.student.sign

import android.content.Intent
import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.utils.Const
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
            val mIntent = intent
            mMapFragment = MapFragment.newInstance().apply {
                val bundle = Bundle().apply {
                    putDouble(
                        Const.BundleKeys.SIGN_LAT,
                        mIntent.getDoubleExtra(
                            Const.BundleKeys.SIGN_LAT,
                            Const.GDUFS_LATLNG.latitude
                        )
                    )
                    putDouble(
                        Const.BundleKeys.SIGN_LNG,
                        mIntent.getDoubleExtra(
                            Const.BundleKeys.SIGN_LNG,
                            Const.GDUFS_LATLNG.longitude
                        )
                    )
                    putString(
                        Const.BundleKeys.SIGN_PLACE,
                        mIntent.getStringExtra(Const.BundleKeys.SIGN_PLACE)
                    )
                    putInt(
                        Const.BundleKeys.SIGN_RADIUS,
                        mIntent.getIntExtra(Const.BundleKeys.SIGN_RADIUS, 500)
                    )
                    putLong(
                        Const.BundleKeys.DETAIL_ID,
                        mIntent.getLongExtra(Const.BundleKeys.DETAIL_ID, 0L)
                    )
                }
                arguments = bundle
            }
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
        PermissionUtils.getInstance()
            .onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQUEST_CODE_FROM_MAP_TO_SCAN_COMPARE -> {
                if (resultCode == Const.RESULT_CODE_COMPARE_SUCCESS) {
                    // 比对成功
                    setFragmentAnimation(R.anim.slide_right_in, R.anim.slide_left_out)
                    hide("Map")
                    show("SignSuccess")
                    mSignSuccessFragment?.setCurrentTime()
                }
            }
        }
    }

    override fun onBackPressed() {
        finish(SignActivity::class.java.name)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }

}