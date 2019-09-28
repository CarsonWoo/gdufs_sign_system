package com.carson.gdufs_sign_system.base

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import java.lang.NullPointerException
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.system.exitProcess


/**
 * 控制app生命周期的Controller
 */
class LifeCallbackManager {
    companion object {

        const val TAG = "LifeCallbackManager"

        private val activityList = ConcurrentLinkedQueue<BaseActivity>()
        private val fragmentList = LinkedList<BaseFragment>()
        // 懒汉单例模式
        private var instance: LifeCallbackManager? = null
            get() {
                if (field == null) {
                    field = LifeCallbackManager()
                }
                return field
            }

        @Synchronized
        fun get(): LifeCallbackManager {
            return instance!!
        }

        /**
         * 判断是否是栈顶活动
         */
        fun isTopActivity(ctx: Context): Boolean {
            val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val tasksInfo = activityManager.getRunningTasks(1)
            if (tasksInfo.size > 0) {
                if (ctx.packageName == tasksInfo[0].topActivity.packageName) {
                    return true
                }
            }
            return false
        }
    }

    fun addFragment(fragment: BaseFragment?) = fragment?.let { fragmentList.add(it) }

    fun removeFragment(fragment: BaseFragment?) = fragment?.let { fragmentList.remove(it) }

    fun addActivity(activity: BaseActivity?) = activity?.let { activityList.add(it) }

    fun removeActivity(activity: BaseActivity?) = activity?.let { activityList.add(it) }

    fun finishActivity(className: String) {
        var target: BaseActivity? = null
        for (activity in activityList) {
            if (activity.javaClass.name == className) {
                target = activity
            }
        }
        // 由此会对该activity执行onDestroy 在onDestroy中执行removeActivity
        target?.finish()
    }

    /**
     * 判断该activity是否在栈中
     */
    fun isExistActivity(className: String) : Boolean {
        for (activity in activityList) {
            if (activity.javaClass.name == className) {
                return true
            }
        }
        return false
    }

    private fun finishAllActive() {
        try {
            for (activity in activityList) {
                activity?.finish()
            }
            activityList.clear()
        } catch (e: NullPointerException) {
            e.printStackTrace()
            Log.e(TAG, ">>> activityList is null")
        }
    }

    fun getTopActivity(ctx: Context): String {
        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasksInfo = activityManager.getRunningTasks(1)
        if (tasksInfo.size > 0) {
            return ctx.packageName
        }
        return ""
    }

    /**
     * 获取栈顶activity
     */
    fun getTopActivity(className: String): BaseActivity? {
        val iterator = activityList.iterator()
        var target : BaseActivity? = null
        while (iterator.hasNext()) {
            val tmp = iterator.next()
            if (tmp.javaClass.name == className) {
                target = tmp
            }
        }
        return target
    }

    fun getFragmentByName(className: String) : BaseFragment? {
        val iterator = fragmentList.iterator()
        var target: BaseFragment? = null
        while (iterator.hasNext()) {
            val tmp = iterator.next()
            if (tmp.javaClass.name == className) {
                target = tmp
            }
        }
        return target
    }

    /**
     * 退出程序 连后台进程也杀死
     */
    fun exitApp(type: Int) {
        finishAllActive()
        if (type == 1) {
            // 异常退出
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        } else {
            // 正常退出
            exitProcess(0)
        }
    }

}