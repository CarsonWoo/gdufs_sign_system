package com.carson.gdufs_sign_system.manager.lookup.controller

import android.util.Log
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.manager.lookup.LookupSingleFragment
import com.carson.gdufs_sign_system.manager.lookup.adapter.LookupSingleItemAdapter
import com.carson.gdufs_sign_system.utils.Const
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LookupSingleController(singleFragment: LookupSingleFragment):
    BaseController<LookupSingleFragment>(singleFragment), CoroutineScope {

    companion object {
        private const val TAG = "LupSgeController"
    }

    private var mJob = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    private lateinit var mAdapter: LookupSingleItemAdapter

    fun getAdapter(): LookupSingleItemAdapter {
        mAdapter = LookupSingleItemAdapter(mutableListOf())
        return mAdapter
    }

    fun loadData() {
        mJob.cancel()

        needGsonConverter(true)

        mJob = executeRequest(
            request = {
                mApiService.getActivityStudentData(mFragment?.arguments
                    ?.getLong(Const.BundleKeys.ACTIVITY_ID)?: 0L).execute()
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

}