package com.carson.gdufs_sign_system.login.controller

import android.content.Intent
import android.text.TextUtils.isEmpty
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.login.IRegisterCallback
import com.carson.gdufs_sign_system.login.LoginActivity
import com.carson.gdufs_sign_system.login.RegisterFragment
import com.carson.gdufs_sign_system.student.main.MainActivity
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.widget.PickerPopupWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.Exception
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class RegisterController(mFragment: RegisterFragment?, private val mIViewCallback: IRegisterCallback):
    BaseController<RegisterFragment?>(mFragment), PickerPopupWindow.OnConfirmPickListener, CoroutineScope {

    private var mJob = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    private var mPickerPopupWindow: PickerPopupWindow? = null

    companion object {
        private const val TAG = "RegisterController"
    }

    fun register(username: String?, password: String?, clazz: String?, userId: String?) {
        if (mFragment?.activity == null) {
            Log.e(TAG, "activity is null do not do register.")
            return
        }
        // 注册操作
        Log.i(TAG, "register")
        if (isEmpty(username) || isEmpty(password) || isEmpty(userId)) {
            Toast.makeText(mFragment?.activity, "please confirm the username & password are both filled.", Toast.LENGTH_SHORT).show()
            return
        }
        if (isEmpty(clazz)) {
            Toast.makeText(mFragment?.activity, "class is empty, please select your class.", Toast.LENGTH_SHORT).show()
            return
        }

        doServerRegister(username!!, password!!, clazz!!, userId!!)
    }

    private fun doServerRegister(phoneNumber: String, password: String, clazz: String, userId: String) {
        mJob.cancel()

        needGsonConverter(true)

        mJob = executeRequest(
            request = {
                val result = mApiService.register(userId, phoneNumber, clazz, password).execute()
                if (result.isSuccessful) {
                    result.body()
                } else {
                    throw Exception(result.message())
                }
            },
            onSuccess = {
                if (it.status == Const.Net.RESPONSE_SUCCESS) {
                    (mFragment?.activity as BaseFragmentActivity?)?.apply {
                        setFragmentAnimation(R.anim.slide_left_in, R.anim.slide_right_out )
                        hide("Register")
                        show("Login")
                        mIViewCallback.onRegisterSuccess()
                    }
                } else {
                    Log.e(TAG, it.msg)
                }
            },
            onFail = {
                Log.e(TAG, it.message)
            }
        )
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

    fun initPopupWindow(anchorView: ViewGroup) {
        mFragment?.context?.apply {
            val mArray = this.resources.getStringArray(R.array.static_class_name)
            if (mPickerPopupWindow == null) {
                mPickerPopupWindow = PickerPopupWindow(WeakReference(this), anchorView,
                    mArray.toMutableList(), this@RegisterController)
            }
            mPickerPopupWindow?.show()
        }
    }

    override fun onConfirmPick(value: String) {
        mIViewCallback.onShowClazz(value)
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }
}