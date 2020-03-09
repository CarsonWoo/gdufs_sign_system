package com.carson.gdufs_sign_system.base

import com.carson.gdufs_sign_system.utils.NetUtils

interface ApiService {

    companion object {
        @Volatile
        private var mInstance: ApiService? = null

        private var mNeedConverter = false

        fun getInstance(url: String, needGsonConverter: Boolean): ApiService =
            if (mNeedConverter != needGsonConverter) {
                mNeedConverter = needGsonConverter
                mInstance = NetUtils.getRetrofitClient(url, ApiService::class.java, needGsonConverter).also { mInstance = it }
                mInstance!!
            } else {
                mInstance ?: synchronized(ApiService::class.java) {
                    mInstance ?: NetUtils.getRetrofitClient(url, ApiService::class.java, needGsonConverter).also { mInstance = it }
                }
            }
    }

}