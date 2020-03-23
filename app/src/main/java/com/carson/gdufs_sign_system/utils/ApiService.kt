package com.carson.gdufs_sign_system.utils

import com.carson.gdufs_sign_system.model.CommonResponse
import com.carson.gdufs_sign_system.model.HomeResponse
import com.carson.gdufs_sign_system.model.LoginResponse
import com.carson.gdufs_sign_system.model.PersonalResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

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
    fun login(@Field("username") username: String,
              @Field("password") password: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("Register")
    fun register(@Field("username") userId: String,
                 @Field("phoneNumber") phoneNumber: String,
                 @Field("class") clazz: String,
                 @Field("password") password: String): Call<CommonResponse>

    @GET("Home")
    fun getHomeData(@Query("username") username: String?): Call<HomeResponse>

    @GET("Personal")
    fun getPersonalData(@Query("username") username: String?): Call<PersonalResponse>

    @FormUrlEncoded
    @POST("UploadFaceResources")
    fun updateFaceResources(@Field("username") username: String?,
                            @Field("faceResources") faceResources: String): Call<CommonResponse>

    @FormUrlEncoded
    @POST("Sign")
    fun signIn(@Field("username") username: String?,
               @Field("programId") programId: Long,
               @Field("signTime") signTime: String): Call<CommonResponse>

}