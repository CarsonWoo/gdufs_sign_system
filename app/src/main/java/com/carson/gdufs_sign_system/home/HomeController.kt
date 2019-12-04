package com.carson.gdufs_sign_system.home

import android.content.Context
import android.graphics.Color
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.user.UserFragment
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.carson.gdufs_sign_system.widget.TabSelector
import kotlinx.android.synthetic.main.activity_home.view.*

class HomeController(private var activity: HomeActivity?): TabSelector.OnTabSelectListener {

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
                mHomeFragment = HomeFragment.newInstance()
                add(R.id.home_container, mHomeFragment!!, "Home")
            } else {
                show(mHomeFragment!!)
            }
            commit()
        }
    }

    fun onDestroy() {
        activity = null
    }

    override fun onTabSelected(position: Int) {
        val trans = mFragmentManager?.beginTransaction()
        // 可以换掉这个动画过渡效果
        trans?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        hideAllFragments()
        when (position) {
            0 -> {
                activity?.let {
                    StatusBarUtil.setStatusBarColor(it, Color.WHITE)
                    StatusBarUtil.setStatusBarDarkTheme(it, true)
                }
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance()
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