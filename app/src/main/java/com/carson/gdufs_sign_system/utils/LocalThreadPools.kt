package com.carson.gdufs_sign_system.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.lang.ref.WeakReference
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.max
import kotlin.math.min

class LocalThreadPools private constructor(context: Context) {

    companion object {
        private const val TAG = "LocalThreadPools"

        private var THREAD_POOL_EXECUTOR: ExecutorService? = null

        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val CORE_POOL_SIZE = max(2, min(CPU_COUNT - 1, 4))
        private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2
        private const val KEEP_ALIVE_SECONDS = 60L
        private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>(8)
        private val sThreadFactory = object: ThreadFactory {
            private val mCount = AtomicInteger(1)

            override fun newThread(r: Runnable?): Thread {
                return Thread(r, "LocalTask #${mCount.getAndIncrement()}")
            }
        }

        private var instance: LocalThreadPools? = null

        fun getInstance(context: Context): LocalThreadPools {
            if (instance == null) {
                instance = LocalThreadPools(context)
            }
            return instance!!
        }
    }

    private fun initThreadPool() {
        val threadPoolExecutor = object : ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory, RejectedHandler()) {
            override fun execute(command: Runnable?) {
                super.execute(command)
                Log.e(TAG, "ActiveCount = $activeCount")
                Log.e(TAG, "PoolSize = $poolSize")
                Log.e(TAG, "Queue = ${queue.size}")
            }
        }

        // 允许核心线程空闲超时时被回收
        threadPoolExecutor.allowCoreThreadTimeOut(true)
        THREAD_POOL_EXECUTOR = threadPoolExecutor
    }

    private open inner class RejectedHandler: RejectedExecutionHandler {
        override fun rejectedExecution(r: Runnable?, executor: ThreadPoolExecutor?) {
            Toast.makeText(mContext?.get(), "当前执行的任务过多，请稍后再试", Toast.LENGTH_SHORT).show()
        }
    }

    private var mContext: WeakReference<Context>? = null

    init {
        mContext = WeakReference(context)
        initThreadPool()
    }

    fun execute(command: Runnable) {
        THREAD_POOL_EXECUTOR?.execute(command)
    }

    /**
     * 通过interrupt方法尝试停止正在执行的任务，但是不保证真的终止正在执行的任务
     * 停止队列中处于等待的任务的执行
     * 不再接收新的任务
     * @return 等待执行的任务列表
     */
    fun shutdownNow(): List<Runnable> {
        return THREAD_POOL_EXECUTOR?.shutdownNow()?: listOf()
    }

    /**
     * 停止队列中处于等待的任务
     * 不再接受新的任务
     * 已经执行的任务会继续执行
     * 如果任务已经执行完了没有必要再调用这个方法
     */
    fun shutDown() {
        THREAD_POOL_EXECUTOR?.shutdown()
        sPoolWorkQueue.clear()
    }



}