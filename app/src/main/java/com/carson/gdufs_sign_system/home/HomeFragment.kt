package com.carson.gdufs_sign_system.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.home.adapter.HomeBannerAdapter
import com.carson.gdufs_sign_system.utils.ScreenUtils
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

        val bannerList = mutableListOf("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png",
            "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_reading.png", "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_class.png",
            "https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_prize.png")
        mPagerAdapter = HomeBannerAdapter(bannerList)
        mPagerAdapter.setUpWithViewPager(mViewPager)
        mPagerAdapter.setUpWithBannerDot(mBannerDot)
    }

    fun onRefresh() {
        // TODO 刷新操作
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mInstance: HomeFragment? = null

        fun newInstance(): HomeFragment {
            if (mInstance == null) mInstance = HomeFragment()
            return mInstance!!
        }
    }
}