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