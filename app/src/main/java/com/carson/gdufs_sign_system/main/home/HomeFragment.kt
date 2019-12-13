package com.carson.gdufs_sign_system.main.home

import android.content.Intent
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
import com.carson.gdufs_sign_system.detail.DetailActivity
import com.carson.gdufs_sign_system.main.MainController
import com.carson.gdufs_sign_system.main.adapter.HomeBannerAdapter
import com.carson.gdufs_sign_system.main.adapter.HomeSignItemAdapter
import com.carson.gdufs_sign_system.main.controller.HomeController
import com.carson.gdufs_sign_system.main.model.SignItem
import com.carson.gdufs_sign_system.widget.BannerDot
import com.carson.gdufs_sign_system.widget.CircleImageView

class HomeFragment private constructor(): BaseFragment() {
    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

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

    private lateinit var mHomeController: HomeController

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
        mHomeController = HomeController(this)
        mAvatar = mRoot.findViewById(R.id.avatar)
        mUsername = mRoot.findViewById(R.id.username)
        mScan = mRoot.findViewById(R.id.home_scan)
        mSearchBar = mRoot.findViewById(R.id.home_search_bar)
        mViewPager = mRoot.findViewById(R.id.view_pager)
        mBannerDot = mRoot.findViewById(R.id.banner_dot)
        mRecyclerview = mRoot.findViewById(R.id.home_recyclerview)



        // banner
        mPagerAdapter = mHomeController.getBannerAdapter()
        mPagerAdapter.setUpWithViewPager(mViewPager)
        mPagerAdapter.setUpWithBannerDot(mBannerDot)
        mPagerAdapter.setBannerClickListener(mHomeController)
        // recycler-view
        mItemAdapter = mHomeController.getItemAdapter()
        mRecyclerview.layoutManager = GridLayoutManager(this.context, 2)
        mRecyclerview.setHasFixedSize(true)
        mRecyclerview.adapter = mItemAdapter

    }

    fun onRefresh() {
        // TODO 刷新操作
    }

    override fun onDestroy() {
        super.onDestroy()
        mHomeController.onDestroy()
    }

    override fun onBackPressed(): Boolean {
        return mHomeController.onBackPressed()
    }

    companion object {
        private const val FRAGMENT_TAG = "Home"
        @JvmStatic
        fun newInstance() = HomeFragment().apply {

        }
    }
}