package com.carson.gdufs_sign_system.manager.lookup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.utils.StatusBarUtil

class LookupActivity : BaseFragmentActivity() {

    private var mLookupTotalFragment: LookupTotalFragment? = null
    private var mLookupSingleFragment: LookupSingleFragment? = null

    override fun getContainerId(): Int {
        return R.id.lookup_container
    }

    override fun getFragmentList(): MutableList<BaseFragment> {
        if (mLookupTotalFragment == null) {
            mLookupTotalFragment = LookupTotalFragment.newInstance()
        }
        if (mLookupSingleFragment == null) {
            mLookupSingleFragment = LookupSingleFragment.newInstance()
        }
        return mutableListOf(mLookupTotalFragment!!, mLookupSingleFragment!!)
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_lookup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
        StatusBarUtil.setStatusBarDarkTheme(this, false)
    }

    override fun onBackPressed() {
        if (mLookupSingleFragment?.let { isFragmentTop(it) } == true) {
            setFragmentAnimation(R.anim.scale_in, R.anim.slide_right_out)
            hide("LookupSingle")
            show("LookupTotal")
        } else {
            finish(this::class.java.name)
            overridePendingTransition(R.anim.scale_in, R.anim.slide_right_out)
        }
    }
}
