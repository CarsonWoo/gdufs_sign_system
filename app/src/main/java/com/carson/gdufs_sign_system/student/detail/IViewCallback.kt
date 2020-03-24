package com.carson.gdufs_sign_system.student.detail

import com.carson.gdufs_sign_system.model.SignDetailBean

interface IViewCallback {
    fun onFabShow(value: Float)

    fun onDataLoaded(data: SignDetailBean)
}