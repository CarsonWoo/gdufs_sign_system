package com.carson.gdufs_sign_system.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.main.adapter.HomeBannerAdapter
import com.carson.gdufs_sign_system.main.adapter.HomeSignItemAdapter
import com.carson.gdufs_sign_system.main.model.SignItem
import com.carson.gdufs_sign_system.widget.BannerDot
import com.carson.gdufs_sign_system.widget.CircleImageView

class HomeFragment private constructor(): BaseFragment() {

    private lateinit var mRoot: View
    private lateinit var mAvatar: CircleImageView
    private lateinit var mUsername: TextView
    private lateinit var mScan: LinearLayout
    private lateinit var mSearchBar: EditText
    private lateinit var mViewPager: ViewPager
    private lateinit var mBannerDot: BannerDot
    private lateinit var mRecyclerview: RecyclerView

    private lateinit var mPagerAdapter: HomeBannerAdapter

    private lateinit var mItemAdapter: HomeSignItemAdapter

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_home, container, false)
        initViews()
        return mRoot
    }

    private fun initViews() {
        mAvatar = mRoot.findViewById(R.id.avatar)
        mUsername = mRoot.findViewById(R.id.username)
        mScan = mRoot.findViewById(R.id.home_scan)
        mSearchBar = mRoot.findViewById(R.id.home_search_bar)
        mViewPager = mRoot.findViewById(R.id.view_pager)
        mBannerDot = mRoot.findViewById(R.id.banner_dot)
        mRecyclerview = mRoot.findViewById(R.id.home_recyclerview)

        // 以下这些数据可以由controller请求
        val bannerList = mutableListOf("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png",
            "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_reading.png", "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_class.png",
            "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_prize.png")
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
        // banner
        mPagerAdapter = HomeBannerAdapter(bannerList)
        mPagerAdapter.setUpWithViewPager(mViewPager)
        mPagerAdapter.setUpWithBannerDot(mBannerDot)
        // recycler-view
        mItemAdapter = HomeSignItemAdapter(itemList)
        mRecyclerview.layoutManager = GridLayoutManager(this.context, 2)
        mRecyclerview.setHasFixedSize(true)
        mRecyclerview.adapter = mItemAdapter

    }

    fun onRefresh() {
        // TODO 刷新操作
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mInstance: HomeFragment? = null

        fun newInstance(): HomeFragment {
            if (mInstance == null) mInstance =
                HomeFragment()
            return mInstance!!
        }
    }
}