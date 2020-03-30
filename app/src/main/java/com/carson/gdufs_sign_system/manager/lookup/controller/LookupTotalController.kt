package com.carson.gdufs_sign_system.manager.lookup.controller

import android.os.Bundle
import android.util.Log
import android.view.View
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.manager.lookup.LookupTotalFragment
import com.carson.gdufs_sign_system.manager.lookup.adapter.LookupTotalItemAdapter
import com.carson.gdufs_sign_system.model.MyActivityItemBean
import com.carson.gdufs_sign_system.utils.Const
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class LookupTotalController(totalFragment: LookupTotalFragment):
    BaseController<LookupTotalFragment>(totalFragment), CoroutineScope, LookupTotalItemAdapter.LookupItemClickCallback {

    companion object {
        private const val TAG = "LookupTotalController"
    }

    private var mJob = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    private lateinit var mAdapter: LookupTotalItemAdapter

    fun getAdapter(): LookupTotalItemAdapter {
        mAdapter = LookupTotalItemAdapter(mutableListOf(), this)
        return mAdapter
    }

    fun loadData() {
        mJob.cancel()

        needGsonConverter(true)

        mJob = executeRequest(
            request = {
                mApiService.getMyActivity(
                    Const.getSharedPreference(WeakReference(mFragment?.context))
                        ?.getString(Const.PreferenceKeys.USER_ID, "")?: "").execute()
            },
            onSuccess = { res ->
                if (res.isSuccessful) {
                    res.body()?.let {
                        mAdapter.setData(it)
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

    override fun onDestroy() {
        mJob.cancel()
        super.onDestroy()
    }

    override fun onItemClick(view: View, id: Long) {
        mFragment?.apply {
            (activity as BaseFragmentActivity?)?.setFragmentAnimation(R.anim.slide_right_in, R.anim.scale_out)
            (activity as BaseFragmentActivity?)?.hide(this)
            val arg = Bundle()
            arg.putLong(Const.BundleKeys.ACTIVITY_ID, id)
            arg.putBoolean(Const.BundleKeys.ACTIVITY_DETAIL_SHOULD_RELOAD, true)
            (activity as BaseFragmentActivity?)?.show("LookupSingle", arg)
        }
    }

}