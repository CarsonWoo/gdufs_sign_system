package com.carson.gdufs_sign_system.student.main.user

interface IViewCallback {
    fun onDataLoaded(username: String, studentId: String, clazz: String, signedNum: String)
}