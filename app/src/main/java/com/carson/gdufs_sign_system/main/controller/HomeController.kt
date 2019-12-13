package com.carson.gdufs_sign_system.main.controller

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.base.LifeCallbackManager
import com.carson.gdufs_sign_system.detail.DetailActivity
import com.carson.gdufs_sign_system.main.MainActivity
import com.carson.gdufs_sign_system.main.adapter.HomeBannerAdapter
import com.carson.gdufs_sign_system.main.adapter.HomeSignItemAdapter
import com.carson.gdufs_sign_system.main.home.HomeFragment
import com.carson.gdufs_sign_system.main.model.SignItem

class HomeController(homeFragment: HomeFragment): BaseController<HomeFragment>(homeFragment), HomeBannerAdapter.OnBannerItemClickListener {
    private lateinit var mBannerAdapter: HomeBannerAdapter
    private lateinit var mItemAdapter: HomeSignItemAdapter

    fun onBackPressed(): Boolean {
        return if (LifeCallbackManager.get().getActivitySize() == 1) {
            false
        } else {
            (mFragment?.activity as BaseFragmentActivity?)?.apply {
                finish(MainActivity::class.java.name)
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
            }
            true
        }
    }

    fun getBannerAdapter(): HomeBannerAdapter {
        // 以下这些数据可以由controller请求
        val bannerList = mutableListOf("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png",
            "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_reading.png", "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_class.png",
            "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_prize.png")
        mBannerAdapter = HomeBannerAdapter(bannerList)
        return mBannerAdapter
    }

    fun getItemAdapter(): HomeSignItemAdapter {
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
        mItemAdapter = HomeSignItemAdapter(itemList)
        return mItemAdapter
    }

    override fun onBannerClick(view: View, url: String, position: Int) {
        val toDetail = Intent(mFragment?.context, DetailActivity::class.java)
        mFragment?.activity?.apply {
            startActivity(toDetail)
            overridePendingTransition(R.anim.slide_right_in, R.anim.scale_out)
        }
    }

}