package com.carson.gdufs_sign_system.student.scan

interface IViewCallback {

    fun onSwitchText(text: String)

    fun onSwitchShadowText(text: String)

    fun onCaptureFailed()

    fun onUploadSuccess()

}