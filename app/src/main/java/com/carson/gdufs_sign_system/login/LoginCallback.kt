package com.carson.gdufs_sign_system.login

import com.carson.gdufs_sign_system.base.BaseActivity

interface LoginCallback {
    /**
     * 登录
     */
    fun login(username: String?, password: String?)

    /**
     * 注册
     */
    fun register(username: String?, password: String?, code: String?)

    /**
     * 去注册页
     */
    fun goToRegister()

    /**
     * 初始化首页
     */
    fun initTab()
}