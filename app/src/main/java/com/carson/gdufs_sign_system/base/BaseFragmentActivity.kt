package com.carson.gdufs_sign_system.base

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.lang.Exception

abstract class BaseFragmentActivity: BaseActivity() {

    companion object {
        private const val TAG = "BaseFragmentActivity"
    }

    private var mTransaction = FragmentTransaction.TRANSIT_FRAGMENT_FADE

    private var mContainerId: Int = 0

    private var mFragmentManager: FragmentManager? = null

    private var mFragmentList: MutableList<out BaseFragment>? = null

    private var mTop = 0

    private var mEnterAnim = 0

    private var mExitAnim = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mFragmentList = getFragmentList()
        this.mContainerId = getContainerId()
        this.mFragmentManager = supportFragmentManager
        if (mContainerId == 0) {
            throw Exception("BaseFragmentActivity must initialize the container id param.")
        }
        if (mFragmentList == null || mFragmentList?.size == 0) {
            throw Exception("BaseFragmentActivity must have at least 2 Fragment objects.")
        }
        if (savedInstanceState == null) {
            initView()
        }
    }

    /**
     * 默认初始化第一个Fragment
     */
    protected open fun initView() {
        mFragmentManager?.beginTransaction()?.apply {
            mFragmentList?.forEachIndexed { index, baseFragment ->
                add(mContainerId, baseFragment, baseFragment.fragmentString())
                if (index != 0) {
                    hide(baseFragment)
                }
            }
            commit()
            mTop = 0
        }
    }

    open fun show(fragment: BaseFragment) {
        mFragmentList?.apply {
            mFragmentManager?.beginTransaction()?.apply {
                if (mEnterAnim != 0 && mExitAnim != 0) {
                    setCustomAnimations(mEnterAnim, mExitAnim)
                } else {
                    setTransition(mTransaction)
                }
                show(fragment)
                commit()
            }
        }
        mFragmentList?.forEachIndexed { index, baseFragment ->
            if (baseFragment == fragment) {
                mTop = index
                return@forEachIndexed
            }
        }
    }

    /**
     * 判断某个fragment是否是在fragmentList的开头
     */
    protected fun isFragmentTop(fragment: BaseFragment): Boolean {
        var pos = -1
        mFragmentList?.forEachIndexed { index, baseFragment ->
            if (baseFragment == fragment) {
                pos = index
                return@forEachIndexed
            }
        }
        return pos == mTop
    }

    fun show(fragmentTag: String?) {
        if (fragmentTag == null) {
            throw Exception("Fragment Tag is Null.")
        }
        mFragmentList?.apply {
            val fragment = mFragmentManager?.findFragmentByTag(fragmentTag) as BaseFragment?
            fragment?.let {
                show(it)
            }
        }
    }

    fun hide(fragmentTag: String?) {
        if (fragmentTag == null) {
            throw Exception("Fragment Tag is Null")
        }
        mFragmentList?.apply {
            val fragment = mFragmentManager?.findFragmentByTag(fragmentTag) as BaseFragment?
            fragment?.let {
                hide(it)
            }
        }
    }

    open fun hide(fragment: BaseFragment) {
        mFragmentManager?.beginTransaction()?.apply {
            if (mEnterAnim != 0 && mExitAnim != 0) {
                setCustomAnimations(mEnterAnim, mExitAnim)
            } else {
                setTransition(mTransaction)
            }
            hide(fragment)
            commit()
        }
    }

    fun hideAllFragments() {
        mFragmentManager?.beginTransaction()?.apply {
//            setTransition(mTransaction)
            mFragmentList?.forEach {
                this.hide(it)
            }
            commit()
        }
    }

    fun setFragmentTransaction(transactionRes: Int) {
        this.mTransaction = transactionRes
    }

    fun setFragmentAnimation(enter: Int, exit: Int) {
        this.mEnterAnim = enter
        this.mExitAnim = exit
    }

    abstract fun getContainerId(): Int

    abstract fun getFragmentList(): MutableList<BaseFragment>

}