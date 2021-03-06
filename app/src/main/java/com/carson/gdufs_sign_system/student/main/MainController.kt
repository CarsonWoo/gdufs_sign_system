package com.carson.gdufs_sign_system.student.main

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.LifeCallbackManager
import com.carson.gdufs_sign_system.student.main.home.HomeFragment
import com.carson.gdufs_sign_system.student.main.user.UserFragment
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.carson.gdufs_sign_system.widget.TabSelector

class MainController(private var activity: MainActivity?): TabSelector.OnTabSelectListener {

    private var mHomeFragment: HomeFragment? = null
    private var mUserFragment: UserFragment? = null
    private var mFragmentManager: FragmentManager? = null

    init {
        mFragmentManager = activity?.supportFragmentManager
    }

    // 先简单点
    fun initView() {
        mFragmentManager?.beginTransaction()?.apply {
            if (mHomeFragment == null) {
                mHomeFragment =
                    HomeFragment.newInstance()
                add(R.id.home_container, mHomeFragment!!, "Home")
            } else {
                show(mHomeFragment!!)
            }
            commit()
        }
        activity?.let {
            StatusBarUtil.setStatusBarColor(it, it.resources.getColor(R.color.colorCommonBackground))
            StatusBarUtil.setStatusBarDarkTheme(it, true)
        }
    }

    fun onDestroy() {
        activity = null
    }
    
    fun onBackPressed(): Boolean {
        return if (LifeCallbackManager.get().getActivitySize() == 1) {
            false
        } else {
            activity?.apply {
                finish(MainActivity::class.java.name)
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
            }
            true
        }
    }

    override fun onTabSelected(position: Int) {
        val trans = mFragmentManager?.beginTransaction()
        // 可以换掉这个动画过渡效果
        trans?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        hideAllFragments()
        when (position) {
            0 -> {
                activity?.let {
                    StatusBarUtil.setStatusBarColor(it, it.resources.getColor(R.color.colorWhite))
                    StatusBarUtil.setStatusBarDarkTheme(it, true)
                }
                if (mHomeFragment == null) {
                    mHomeFragment =
                        HomeFragment.newInstance()
                    trans?.add(R.id.home_container, mHomeFragment!!, "Home")
                } else {
                    mHomeFragment?.let {
                        trans?.show(it)
                    }
                }
                trans?.commit()
            }
            1 -> {
                activity?.let {
                    StatusBarUtil.setStatusBarColor(it, it.resources.getColor(R.color.colorCyan))
                    StatusBarUtil.setStatusBarDarkTheme(it, false)
                }
                if (mUserFragment == null) {
                    mUserFragment = UserFragment.newInstance()
                    trans?.add(R.id.home_container, mUserFragment!!, "User")
                } else {
                    mUserFragment?.let {
                        trans?.show(it)
                    }
                }
                trans?.commit()
            }
        }
    }

    // 可做刷新操作
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

    private fun hideAllFragments() {
        mFragmentManager?.beginTransaction()?.apply {
            mHomeFragment?.let {
                hide(it)
            }
            mUserFragment?.let {
                hide(it)
            }
            commit()
        }
    }
}