package com.carson.gdufs_sign_system.login

interface IRegisterCallback {
    fun onShowClazz(clazz: String?)

    /**
     * 需要清空所有内容
     */
    fun onRegisterSuccess()
}