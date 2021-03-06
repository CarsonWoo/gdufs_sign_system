package com.carson.gdufs_sign_system.utils

import com.carson.gdufs_sign_system.model.*
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
                 @Field("name") name: String,
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

    @FormUrlEncoded
    @POST("GetProgramWithId")
    fun getDetailData(@FieldMap dataFields: HashMap<String, Any?>): @JvmSuppressWildcards Call<SignDetailBean>

    @FormUrlEncoded
    @POST("GetSigningWithAuthor")
    fun getMyActivity(@Field("username") username: String): Call<MutableList<MyActivityItemBean>>

    @FormUrlEncoded
    @POST("GetSigningStatus")
    fun getActivityStudentData(@Field("signingId") signingId: Long): Call<MutableList<MyActivityStudentItemBean>>

    @FormUrlEncoded
    @POST("PublishSigning")
    fun post(@FieldMap dataFields: HashMap<String, Any?>): @JvmSuppressWildcards Call<CommonResponse>

}