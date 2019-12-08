package com.carson.gdufs_sign_system.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import java.util.concurrent.ConcurrentHashMap

/**
 * Activity基类
 */
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val TAG = "BaseActivity"
        // 两次点击back的间隔
        const val BACK_INTERVAL = 3 * 1000
    }

    private var mPressedBackTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())

        setStatusBar()

        LifeCallbackManager.get().addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        LifeCallbackManager.get().removeActivity(this)
    }

    override fun onBackPressed() {
        val pressTime = System.currentTimeMillis()
        if (pressTime - mPressedBackTime < BACK_INTERVAL) {
            mPressedBackTime = 0
            LifeCallbackManager.get().exitApp(0)
        } else {
            mPressedBackTime = pressTime
            Toast.makeText(this, R.string.back_confirm_toast, Toast.LENGTH_SHORT).show()
        }
    }

    open fun finish(clazzName: String) {
        LifeCallbackManager.get().finishActivity(clazzName)
    }

    /**
     * 如果不需要的话可以复写 直接为空方法
     */
    open fun setStatusBar() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        // 设置透明
        StatusBarUtil.setTranslucentStatus(this)
        // 设置深色主题
//        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
//            StatusBarUtil.setStatusBarColor(this, 0x55000000)
//        }
        StatusBarUtil.setStatusBarColor(this, 0x55000000)
    }

    abstract fun getContentViewResId(): Int
}