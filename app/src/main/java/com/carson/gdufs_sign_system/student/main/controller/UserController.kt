package com.carson.gdufs_sign_system.student.main.controller

import android.content.Intent
import android.util.Log
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.login.LoginActivity
import com.carson.gdufs_sign_system.model.SignBean
import com.carson.gdufs_sign_system.student.main.MainActivity
import com.carson.gdufs_sign_system.student.main.adapter.UserSignItemAdapter
import com.carson.gdufs_sign_system.student.main.user.IViewCallback
import com.carson.gdufs_sign_system.student.main.user.UserFragment
import com.carson.gdufs_sign_system.utils.Const
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class UserController constructor(userFragment: UserFragment, private val mIView: IViewCallback):
    BaseController<UserFragment>(userFragment), CoroutineScope {

    companion object {
        private const val TAG = "UserController"
    }

    private lateinit var mAdapter: UserSignItemAdapter

    private var mJob = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    fun getUserItemAdapter(): UserSignItemAdapter {
        val itemList = mutableListOf<SignBean>()
        mAdapter = UserSignItemAdapter(itemList)
        return mAdapter
    }

    fun logout() {
        Const.getSharedPreference(WeakReference(mFragment?.context))?.edit()?.clear()?.apply()
        (mFragment?.activity as MainActivity?)?.let {
            Intent(it, LoginActivity::class.java).apply {
                it.startActivity(this)
                (it as BaseActivity?)?.finish(MainActivity::class.java.name)
            }
        }
    }

    fun loadPersonalData() {
        mJob.cancel()

        needGsonConverter(true)

        mJob = executeRequest(
            request = {
                mApiService.getPersonalData(Const.getSharedPreference(WeakReference(mFragment?.context))
                    ?.getString(Const.PreferenceKeys.USER_ID, "")).execute()
            },
            onSuccess = { res ->
                if (res.isSuccessful) {
                    res.body()?.let {
                        mIView.onDataLoaded(it.name, it.userId, it.userClass, it.signedList.size.toString())
                        mAdapter.setData(it.signedList)
                        mAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e(TAG, res.message())
                }
            },
            onFail = {
                Log.e(TAG, it.message)
            }
        )
    }

}