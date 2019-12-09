package com.carson.gdufs_sign_system.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Fragment基类
 */
abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LifeCallbackManager.get().addFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getContentView(inflater, container, savedInstanceState)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    // 懒加载可以在这里操作
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun onDestroy() {
        super.onDestroy()
        LifeCallbackManager.get().removeFragment(this)
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    protected abstract fun getContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    abstract fun fragmentString(): String

    companion object {
        private val TAG = "BaseFragment"
    }
}