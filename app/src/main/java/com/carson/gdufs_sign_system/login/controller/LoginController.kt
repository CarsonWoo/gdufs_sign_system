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
import com.carson.gdufs_sign_system.utils.ApiService
import com.carson.gdufs_sign_system.utils.Const
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.URLDecoder
import kotlin.coroutines.CoroutineContext

class LoginController(mFragment: LoginFragment?) : BaseController<LoginFragment?>(mFragment),
    CoroutineScope {

    private var mJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

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
            Toast.makeText(mFragment?.activity, "please fill the blank info.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        doServerLogin(username!!, password!!)
    }

    private fun doServerLogin(username: String, password: String) {
        mJob.cancel()

        needGsonConverter(true)

        mJob = executeRequest(
            request = {
                /*val result = */mApiService.login(username, password).execute()
//                if (result.isSuccessful) {
//                    return@executeRequest result.body()
//                } else {
////                    throw Exception(result.message())
//                    return@executeRequest result.body()
//                }
            },
            onSuccess = { res ->
                if (res.isSuccessful) {
                    res.body()?.let {
                        Log.i(
                            TAG, "status: ${it.status} msg: ${it.msg} identity: ${it.identity} " +
                                    "authImageBase: ${it.authImageBase} userId: ${it.userId}"
                        )
                        if (it.status == Const.Net.RESPONSE_SUCCESS) {
                            // 还要记录sp
                            val mSharedPreferences =
                                Const.getSharedPreference(WeakReference(mFragment?.context))
                            mSharedPreferences?.edit()?.apply {
                                putString(Const.PreferenceKeys.USER_ID, it.userId)
                                putString(Const.PreferenceKeys.AUTH_IMAGE, it.authImageBase)
                                putString(Const.PreferenceKeys.IDENTITY, it.identity)
                                apply()
                            }
                            jumpToMain(it.identity)
                        } else {
                            Toast.makeText(mFragment?.context, it.msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(mFragment?.context, res.message(), Toast.LENGTH_SHORT).show()
                }
            },
            onFail = {
                Log.e(TAG, it.message)
                Toast.makeText(mFragment?.context, it.message, Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun jumpToMain(identity: String?) {
        if (identity == "0") {
            // 学生端
            // to student
            (mFragment?.activity as BaseActivity?)?.let {
                val toMain = Intent(it, MainActivity::class.java)
                // set data bundle
                it.startActivity(toMain)
                it.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
                it.finish(LoginActivity::class.java.name)
            }
        } else {
            // 管理端
            // to manager
            (mFragment?.activity as BaseActivity?)?.let {
                val toManage = Intent(it, ManageActivity::class.java)
                it.startActivity(toManage)
                it.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
                it.finish(LoginActivity::class.java.name)
            }
        }
    }

    fun goToRegister() {
        (mFragment?.activity as BaseFragmentActivity?)?.apply {
            setFragmentAnimation(R.anim.slide_right_in, R.anim.slide_left_out)
            hide("Login")
            show("Register")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob?.cancel()
    }
}