package com.carson.gdufs_sign_system.student.main

import android.content.Intent
import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.student.main.home.HomeFragment
import com.carson.gdufs_sign_system.student.main.user.UserFragment
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.carson.gdufs_sign_system.widget.TabSelector

class MainActivity: BaseFragmentActivity(), TabSelector.OnTabSelectListener {

    private lateinit var mTabSelector: TabSelector
    private var mHomeFragment: HomeFragment? = null
    private var mUserFragment: UserFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        mTabSelector = findViewById(R.id.tab_selector)
        initEvent()
    }

    private fun initEvent() {
        mTabSelector.setSelectListener(this)
        if (isNightMode()) {
            mTabSelector.setBackgroundResource(R.drawable.bg_tabbar_night)
            setDarkStatusBar()
        } else {
            mTabSelector.setBackgroundResource(R.drawable.bg_tabbar)
            StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCommonBackground))
            StatusBarUtil.setStatusBarDarkTheme(this, true)
        }
    }

    override fun onBackPressed() {
        if (mHomeFragment?.let { isFragmentTop(it) } == true) {
            if (mHomeFragment?.onBackPressed() == false) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onTabSelected(position: Int) {
        when (position) {
            0 -> {
                if (isNightMode()) {
                    setDarkStatusBar()
                } else {
                    StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorWhite))
                    StatusBarUtil.setStatusBarDarkTheme(this, true)
                }
                hide(mUserFragment?.fragmentString())
                show(mHomeFragment?.fragmentString())
            }
            1 -> {
                StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
                StatusBarUtil.setStatusBarDarkTheme(this, false)
                hide(mHomeFragment?.fragmentString())
                show(mUserFragment?.fragmentString())
            }
        }
    }

    override fun onTabReselected(position: Int) {
        when (position) {
            0 -> {
                mHomeFragment?.onRefresh()
            }
            1 -> {
                mUserFragment?.onRefresh()
            }
        }
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_home
    }

    override fun getContainerId(): Int {
        return R.id.home_container
    }

    override fun getFragmentList(): MutableList<BaseFragment> {
        if (mHomeFragment == null) {
            mHomeFragment = HomeFragment.newInstance()
        }
        if (mUserFragment == null) {
            mUserFragment = UserFragment.newInstance()
        }
        return mutableListOf(mHomeFragment!!, mUserFragment!!)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Const.REQUEST_CODE_FROM_HOME_TO_DETAIL &&
                resultCode == Const.RESULT_CODE_SIGN_SUCCESS) {
            mHomeFragment?.onRefresh()
            mUserFragment?.onRefresh()
        }
    }

}