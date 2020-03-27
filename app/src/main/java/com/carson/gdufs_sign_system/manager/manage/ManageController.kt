package com.carson.gdufs_sign_system.manager.manage

import android.app.Activity
import com.carson.gdufs_sign_system.base.BaseManageController
import kotlinx.coroutines.*
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class ManageController(activity: WeakReference<ManageActivity>, private val mIView: IViewCallback):
    BaseManageController<ManageActivity>(activity), CoroutineScope {

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

    override fun onDestroy() {
        mJob.cancel()
        super.onDestroy()
    }
}