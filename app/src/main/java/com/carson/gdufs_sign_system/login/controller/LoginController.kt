package com.carson.gdufs_sign_system.login.controller

import android.content.Intent
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.Toast
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.login.LoginActivity
import com.carson.gdufs_sign_system.login.LoginFragment
import com.carson.gdufs_sign_system.manager.manage.ManageActivity
import com.carson.gdufs_sign_system.student.main.MainActivity

class LoginController(mFragment: LoginFragment?): BaseController<LoginFragment?>(mFragment) {

    companion object {
        private const val TAG = "LoginController"
    }

    fun login(username: String?, password: String?) {
        if (mFragment?.activity == null) {
            Log.e(TAG, "activity is null do not do login.")
            return
        }
        // 登录操作
        Log.i(TAG, "login")
        if (isEmpty(username) || isEmpty(password)) {
            Toast.makeText(mFragment?.activity, "please fill the blank info.", Toast.LENGTH_SHORT).show()
            return
        }

        // 跳转
        jumpToMain()

    }

    private fun jumpToMain() {
        // to student
//        (mFragment?.activity as BaseActivity?)?.let {
//            val toMain = Intent(it, MainActivity::class.java)
//            // set data bundle
//            it.startActivity(toMain)
//            it.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
//            it.finish(LoginActivity::class.java.name)
//        }
        // to manager
        (mFragment?.activity as BaseActivity?)?.let {
            val toManage = Intent(it, ManageActivity::class.java)
            it.startActivity(toManage)
            it.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
            it.finish(LoginActivity::class.java.name)
        }
    }

    fun goToRegister() {
        (mFragment?.activity as BaseFragmentActivity?)?.apply {
            setFragmentAnimation(R.anim.slide_right_in, R.anim.slide_left_out)
            hide("Login")
            show("Register")
        }
    }

}