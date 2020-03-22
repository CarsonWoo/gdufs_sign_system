package com.carson.gdufs_sign_system.student.main.controller

import android.content.Intent
import android.util.Log
import android.view.View
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.base.LifeCallbackManager
import com.carson.gdufs_sign_system.model.SignBean
import com.carson.gdufs_sign_system.student.detail.DetailActivity
import com.carson.gdufs_sign_system.student.main.MainActivity
import com.carson.gdufs_sign_system.student.main.adapter.HomeBannerAdapter
import com.carson.gdufs_sign_system.student.main.adapter.HomeSignItemAdapter
import com.carson.gdufs_sign_system.student.main.home.HomeFragment
import com.carson.gdufs_sign_system.student.main.model.SignItem
import com.carson.gdufs_sign_system.utils.Const
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class HomeController(homeFragment: HomeFragment): BaseController<HomeFragment>(homeFragment),
    HomeBannerAdapter.OnBannerItemClickListener, CoroutineScope, HomeSignItemAdapter.OnSignClickListener {

    companion object {
        private const val TAG = "HomeController"
    }

    private var mJob = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    private lateinit var mBannerAdapter: HomeBannerAdapter
    private lateinit var mItemAdapter: HomeSignItemAdapter

    fun onBackPressed(): Boolean {
        return if (LifeCallbackManager.get().getActivitySize() == 1) {
            false
        } else {
            (mFragment?.activity as BaseFragmentActivity?)?.apply {
                finish(MainActivity::class.java.name)
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
            }
            true
        }
    }

    fun getBannerAdapter(): HomeBannerAdapter {
        // 以下这些数据可以由controller请求
        val bannerList = mutableListOf<String>()
        mBannerAdapter = HomeBannerAdapter(bannerList)
        return mBannerAdapter
    }

    fun getItemAdapter(): HomeSignItemAdapter {
        val itemList = mutableListOf<SignBean>()
        mItemAdapter = HomeSignItemAdapter(itemList)
        mItemAdapter.setSignClickListener(this)
        return mItemAdapter
    }

    override fun onBannerClick(view: View, url: String, position: Int) {

    }

    fun loadData() {
        mJob.cancel()

        needGsonConverter(true)

        mJob = executeRequest(
            request = {
                mApiService.getHomeData(Const.getSharedPreference(WeakReference(mFragment?.context))
                    ?.getString(Const.PreferenceKeys.USER_ID, "")).execute()
            },
            onSuccess = { res ->
                if (res.isSuccessful) {
                    res.body()?.let {
                        Log.i(TAG, it.bannerList.size.toString() + " " + it.signingList.size)
                        mBannerAdapter.setData(it.bannerList)
                        mBannerAdapter.notifyDataSetChanged()
                        mItemAdapter.setData(it.signingList)
                        mItemAdapter.notifyDataSetChanged()
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

    override fun onSignClick(id: Long) {
        val toDetail = Intent(mFragment?.context, DetailActivity::class.java).apply {
            putExtra("id", id)
        }
        mFragment?.activity?.apply {
            startActivity(toDetail)
            overridePendingTransition(R.anim.slide_right_in, R.anim.scale_out)
        }
    }

    override fun onDestroy() {
        mBannerAdapter.onDestroy()
        super.onDestroy()
    }

}