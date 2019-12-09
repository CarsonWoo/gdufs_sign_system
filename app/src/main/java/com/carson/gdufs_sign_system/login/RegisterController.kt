package com.carson.gdufs_sign_system.login

import android.content.Intent
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.View
import android.widget.Toast
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.main.MainActivity

class RegisterController(mFragment: RegisterFragment?): BaseController<RegisterFragment?>(mFragment) {

    companion object {
        private const val TAG = "RegisterController"
    }

    fun register(username: String?, password: String?, code: String?) {
        if (mFragment?.activity == null) {
            Log.e(TAG, "activity is null do not do register.")
            return
        }
        // 注册操作
        Log.i(TAG, "register")
        if (isEmpty(username) || isEmpty(password)) {
            Toast.makeText(mFragment?.activity, "please confirm the username & password are both filled.", Toast.LENGTH_SHORT).show()
            return
        }
        if (isEmpty(code)) {
            Toast.makeText(mFragment?.activity, "code auth error. please make sure the code is correct.", Toast.LENGTH_SHORT).show()
            return
        }

        jumpToMain()
    }

    private fun jumpToMain() {
        (mFragment?.activity as BaseActivity?)?.let {
            val toMain = Intent(it, MainActivity::class.java)
            // set data bundle
            it.startActivity(toMain)
            it.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
            it.finish(LoginActivity::class.java.name)
        }
    }

    fun onBackPressed(): Boolean {
        (mFragment?.activity as BaseFragmentActivity?)?.apply {
            setFragmentAnimation(R.anim.slide_left_in, R.anim.slide_right_out )
            hide("Register")
            show("Login")
            return true
        }
        return false
    }
}