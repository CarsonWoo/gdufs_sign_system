package com.carson.gdufs_sign_system.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

object NetUtils {
    fun <T> getRetrofitClient(url: String, clazz: Class<T>, needGsonConverter: Boolean): T {
        val builder = Retrofit.Builder()
        builder.baseUrl(url)
        if (needGsonConverter) builder.addConverterFactory(GsonConverterFactory.create())
        return builder.build().create(clazz)
    }
}