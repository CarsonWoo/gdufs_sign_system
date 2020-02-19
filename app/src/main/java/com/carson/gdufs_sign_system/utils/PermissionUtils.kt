package com.carson.gdufs_sign_system.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.SparseArray
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.concurrent.ConcurrentHashMap

class PermissionUtils private constructor() {

    private enum class STATE {
        GRANTED, // 同意
        DENIED, // 拒绝
        ALWAYS_DENIED // 不再提示
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mInstance: PermissionUtils? = null

        fun getInstance(): PermissionUtils {
            if (mInstance == null) {
                synchronized(PermissionUtils::class.java) {
                    mInstance = PermissionUtils()
                }
            }
            checkNotNull(mInstance) { "instance should not be null" }
            return mInstance!!
        }
    }

    private lateinit var mActivity: Activity
    private var mRequestCode = -1
    private var mCallback: PermissionCallback? = null
    private lateinit var mPermissions: Array<out String>

    private val mPermissionStatusList = ConcurrentHashMap<String, STATE>()

    fun destroy() {
        if (mInstance != null) {
            mInstance = null
        }
    }

    fun with(_object: Any): PermissionUtils {
        if (_object is Activity) {
            mActivity = _object
            return this
        }
        if (_object is Fragment) {
            checkNotNull(_object.activity) { "fragment activity is null, cannot init permissionUtils" }
            mActivity = _object.activity!!
            return this
        }
        throw IllegalArgumentException("_object is not fragment or activity, cannot init permissionUtils")
    }

    fun requestCode(requestCode: Int): PermissionUtils {
        require(requestCode >= 0) { "request code should be unless as 0" }
        this.mRequestCode = requestCode
        return this
    }

    fun permissions(vararg permissions: String) {
        this.mPermissions = permissions
    }

    fun request(callback: PermissionCallback) {
        this.mCallback = callback
        if (Build.VERSION.SDK_INT >= 23) {
            var isAllGranted = true
            mPermissions.forEach {
                if (ContextCompat.checkSelfPermission(mActivity, it) == PackageManager.PERMISSION_DENIED) {
                    isAllGranted = false

                }
            }
        } else {
            mCallback?.granted()
        }
    }

    interface PermissionCallback {
        fun granted()

        fun denied()
    }
}