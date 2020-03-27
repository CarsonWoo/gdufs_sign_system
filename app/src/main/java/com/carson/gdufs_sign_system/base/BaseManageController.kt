package com.carson.gdufs_sign_system.base

import android.util.Log
import com.carson.gdufs_sign_system.utils.ApiService
import com.carson.gdufs_sign_system.utils.Const
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.ref.WeakReference

abstract class BaseManageController<T: BaseActivity>(protected val mActivityRef: WeakReference<T>) {

    protected var mApiService = ApiService.getInstance(Const.BASE_URL, false)

    protected fun needGsonConverter(needGson: Boolean) {
        mApiService = ApiService.getInstance(Const.BASE_URL, needGson)
    }

    protected fun <E> executeRequest(request: suspend () -> E?, onSuccess: (E) -> Unit = {},
                                     onFail: (Throwable) -> Unit = {}): Job {
        val uiScope = CoroutineScope(Dispatchers.Main)
        return uiScope.launch {
            try {
                val res: E? = withContext(Dispatchers.IO) {
                    request()
                }
                res?.let {
                    onSuccess(it)
                }
            } catch (e: CancellationException) {
                Log.e("executeRequest", " >>> job cancelled.")
            } catch (e: Exception) {
                Log.e("executeRequest", " >>> request caused Exception")
                onFail(e)
            }
        }
    }

    open fun onDestroy() {
        mActivityRef.clear()
    }
}