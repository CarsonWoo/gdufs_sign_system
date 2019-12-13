package com.carson.gdufs_sign_system.main.controller

import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.main.adapter.UserSignItemAdapter
import com.carson.gdufs_sign_system.main.model.SignItem
import com.carson.gdufs_sign_system.main.user.UserFragment

class UserController constructor(userFragment: UserFragment): BaseController<UserFragment>(userFragment) {
    private lateinit var mAdapter: UserSignItemAdapter

    fun getUserItemAdapter(): UserSignItemAdapter {
        val itemList = mutableListOf(
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png", "activity 1",
                "2019/11/30 14:00-18:00", 38),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_reading.png", "activity 2",
                "2019/11/30 14:00-18:00", 20),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png", "activity 3",
                "2019/11/30 14:00-18:00", 0),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_prize.png", "activity 4",
                "2019/11/30 14:00-18:00", 55),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_reading.png", "activity 5",
                "2019/11/30 14:00-18:00", 18),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png", "activity 6",
                "2019/11/30 14:00-18:00", 38),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png", "activity 7",
                "2019/11/30 14:00-18:00", 68),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_class.png", "activity 8",
                "2019/11/30 14:00-18:00", 68),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png", "activity 9",
                "2019/11/30 14:00-18:00", 68),
            SignItem("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_class.png", "activity 10",
                "2019/11/30 14:00-18:00", 68)
        )
        mAdapter = UserSignItemAdapter(itemList)
        return mAdapter
    }
}