package com.carson.gdufs_sign_system.base

import android.util.Log
import com.carson.gdufs_sign_system.utils.Const
import kotlinx.coroutines.*
import java.lang.Exception

abstract class BaseController<T: BaseFragment?>(protected var mFragment: T?) {

    protected val mApiService = ApiService.getInstance(Const.BASE_URL, true)

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
        mFragment = null
    }
}