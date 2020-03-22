package com.carson.gdufs_sign_system.login

import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.utils.Const
import java.lang.ref.WeakReference

class LoginActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
    }

    override fun getFragmentList(): MutableList<BaseFragment> {
        if (mLoginFragment == null) {
            mLoginFragment = LoginFragment.newInstance()
        }
        if (mRegisterFragment == null) {
            mRegisterFragment = RegisterFragment.newInstance()
        }
        return mutableListOf(mLoginFragment!!, mRegisterFragment!!)
    }

    override fun getContainerId(): Int {
        return R.id.container
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_login
    }

    private var mLoginFragment: LoginFragment? = null
    private var mRegisterFragment: RegisterFragment? = null

    override fun onBackPressed() {
        if (mLoginFragment?.let { return@let isFragmentTop(it) } == true) {
            super.onBackPressed()
        } else {
            mRegisterFragment?.onBackPressed()
        }
    }

}
