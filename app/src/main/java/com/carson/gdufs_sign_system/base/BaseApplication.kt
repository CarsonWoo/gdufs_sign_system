package com.carson.gdufs_sign_system.base

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.carson.gdufs_sign_system.login.LoginActivity
import com.carson.gdufs_sign_system.utils.Const
import java.lang.ref.WeakReference

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Const.getSharedPreference(WeakReference(applicationContext))?.let { sp ->
            AppCompatDelegate.setDefaultNightMode(sp.getInt(Const.PreferenceKeys.DAYNIGHT_MODE,
                AppCompatDelegate.MODE_NIGHT_NO))
        }

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (savedInstanceState != null) {
                    // 重新启动
                    val reStartIntent = Intent(activity, LoginActivity::class.java)
                    reStartIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    activity?.startActivity(reStartIntent)
                }
            }

        })
    }


}