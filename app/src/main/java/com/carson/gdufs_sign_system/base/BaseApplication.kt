package com.carson.gdufs_sign_system.base

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.carson.gdufs_sign_system.login.LoginActivity

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
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