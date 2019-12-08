package com.carson.gdufs_sign_system.login

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.LifeCallbackManager
import com.carson.gdufs_sign_system.main.HomeActivity

class LoginController(private var activity: BaseActivity?) : LoginCallback {

    companion object {
        const val TAG = "LoginController"
    }

    private var mLoginFragment: LoginFragment? = null
    private var mRegisterFragment: RegisterFragment? = null
    private var mFragmentManager: FragmentManager? = null

    private val mFragmentList = ArrayList<BaseFragment>()

    init {
        mFragmentManager = activity?.supportFragmentManager
    }

    override fun login(username: String?, password: String?) {
        if (activity == null) {
            Log.e(TAG, "activity is null do not do login.")
            return
        }
        // 登录操作
        Log.i(TAG, "login")
        if (isEmpty(username) || isEmpty(password)) {
            Toast.makeText(activity, "please fill the blank info.", Toast.LENGTH_SHORT).show()
            return
        }

        // 跳转
        jumpToMain()

    }

    private fun jumpToMain() {
        activity?.let {
            val toMain = Intent(it, HomeActivity::class.java)
            // set data bundle
            it.startActivity(toMain)
            it.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
            it.finish(LoginActivity::class.java.name)
        }
    }

    override fun register(username: String?, password: String?, code: String?) {
        if (activity == null) {
            Log.e(TAG, "activity is null do not do register.")
            return
        }
        // 注册操作
        Log.i(TAG, "register")
        if (isEmpty(username) || isEmpty(password)) {
            Toast.makeText(activity, "please confirm the username & password are both filled.", Toast.LENGTH_SHORT).show()
            return
        }
        if (isEmpty(code)) {
            Toast.makeText(activity, "code auth error. please make sure the code is correct.", Toast.LENGTH_SHORT).show()
            return
        }

        jumpToMain()
    }

    override fun goToRegister() {
        hideTab(arrayOf(mLoginFragment, mRegisterFragment))
        if (activity == null) return
        val fragmentManager = activity?.supportFragmentManager
        val transaction = fragmentManager?.beginTransaction()
        Log.i(TAG, "go to register")
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if (mRegisterFragment == null) {
            mRegisterFragment = RegisterFragment.newInstance(this)
            mRegisterFragment?.let {
                transaction?.add(R.id.container, it, "Register")
                mFragmentList.add(0, it)
            }
        } else {
            mRegisterFragment?.let {
                transaction?.show(it)
                mFragmentList.add(0, it)
            }
        }
        transaction?.commitAllowingStateLoss()
    }

    /**
     * 页面初始化时将logintab加载
     */
    override fun initTab() {
        hideTab(arrayOf(mLoginFragment, mRegisterFragment))
        val transaction = mFragmentManager?.beginTransaction()
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if (mLoginFragment == null) {
            mLoginFragment = LoginFragment.newInstance(this)
            mLoginFragment?.let {
                transaction?.add(R.id.container, it, "Login")
                mFragmentList.add(it)
            }
        } else {
            mLoginFragment?.let {
                transaction?.show(it)
            }
        }
        transaction?.commitAllowingStateLoss()
    }

    fun onBackPressed(): Boolean {
        return if (isRegisterTop()) {
            // 点击返回会登录态
            mFragmentList.removeAt(0)
            hideTab(arrayOf(mRegisterFragment))
            showTab(mLoginFragment as BaseFragment)
            true
        } else {
            false
        }
    }

    private fun showTab(fragment: BaseFragment) {
        val trans = mFragmentManager?.beginTransaction()
        trans?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragment.let {
            trans?.show(it)
        }
        trans?.commit()
    }

    private fun isRegisterTop(): Boolean {
        return mFragmentList[0] is RegisterFragment
    }

    private fun hideTab(fragments: Array<BaseFragment?>) {
        val trans = mFragmentManager?.beginTransaction()
        trans?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragments.forEach {
            it?.let {
                trans?.hide(it)
            }
        }
        trans?.commit()
    }

    private fun isEmpty(field: String?): Boolean {
        return TextUtils.isEmpty(field)
    }

    fun onDestroy() {
        activity = null
    }

}