package com.carson.gdufs_sign_system.manager.manage

import android.app.Activity
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class ManageController(activity: ManageActivity, private val mIView: IViewCallback): CoroutineScope {
    private var mActivity = WeakReference<Activity>(activity)

    private var mJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    fun requestNetwork() {
//        launch {
//            mIView.onShowText("开始请求网络", 0)
//            val result = withContext(Dispatchers.IO) {
//                ApiService.getInstance("https://api.github.com/", true).getListRepos("defunkt").execute()
//            }
////            Log.e("ManageController", "result = $result")
//            if (result.isSuccessful) {
//                result.body()?.forEach {
//                    mIView.onShowText(it.fullName + "\n", 1)
//                }
//            }
////            val result = withContext(Dispatchers.IO) {
////                call.execute()
////            }
//            Log.e("ManageController", "result = $result")
//        }

    }

    fun onDestroy() {
        mJob.cancel()
        mActivity.clear()
    }
}