package com.carson.gdufs_sign_system.model

data class CommonResponse(
    val status: String,
    val msg: String
)

data class LoginResponse(
    val status: String,
    val msg: String,
    val identity: String,
    val authImageBase: String,
    val userId: String
)

data class HomeResponse(
    val bannerList: MutableList<String>,
    val signingList: MutableList<SignBean>
)

data class PersonalResponse(
    val userId: String,
    val userClass: String,
    val name: String,
    val signedList: MutableList<SignBean>
)

data class SignBean(
    val id: Long,
    val signingTime: String,
    val status: String,
    val name: String,
    val startTime: String,
    val endTime: String,
    val num: Int,
    val picUrl: String
)

data class SignDetailBean(
    val programName: String,
    val startTime: String,
    val endTime: String,
    val latitude: Double,
    val longtitude: Double,
    val place: String,
    val range: Int,
    val status: String,
    val picUrl: String,
    val signedNum: Int,
    val totalNum: Int
)

// 所有签到活动查看
data class MyActivityItemBean(
    val signingId: Long,
    val name: String,
    val startTime: String,
    val endTime: String,
    val picUrl: String
)

// 单个签到活动学生查看
data class MyActivityStudentItemBean(
    val number: String,
    val name: String,
    val classes: String,
    val status: String
)