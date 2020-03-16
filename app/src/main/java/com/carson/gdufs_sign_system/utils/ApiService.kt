package com.carson.gdufs_sign_system.utils

import com.carson.gdufs_sign_system.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
                mInstance ?: synchronized(
                    ApiService::class.java) {
                    mInstance ?: NetUtils.getRetrofitClient(url, ApiService::class.java, needGsonConverter).also { mInstance = it }
                }
            }
    }

    @FormUrlEncoded
    @POST("Login")
    fun login(@Field("username") username: String, @Field("password") password: String): Call<LoginResponse>

}