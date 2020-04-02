package com.carson.gdufs_sign_system.student.main.home

import com.carson.gdufs_sign_system.model.SignBean

interface IViewCallback {
    fun onDataLoaded(mItemList: MutableList<SignBean>)
}