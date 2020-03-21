package com.carson.gdufs_sign_system.student.main.home

import android.Manifest
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
import com.carson.gdufs_sign_system.student.main.adapter.HomeBannerAdapter
import com.carson.gdufs_sign_system.student.main.adapter.HomeSignItemAdapter
import com.carson.gdufs_sign_system.student.main.controller.HomeController
import com.carson.gdufs_sign_system.student.scan.ScanActivity
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.widget.BannerDot
import com.carson.gdufs_sign_system.widget.CircleImageView
import com.carson.gdufs_sign_system.widget.RoundImageView

class HomeFragment: BaseFragment() {
    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

    private lateinit var mRoot: View
    private lateinit var mAvatar: RoundImageView
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
        initEvents()
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

    private fun initEvents() {
        mScan.setOnClickListener {
            PermissionUtils.getInstance()
                .with(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                .requestCode(PermissionUtils.CODE_MULTI)
                .request(object : PermissionUtils.PermissionCallback {
                    override fun denied() {
                        PermissionUtils.getInstance().showDialog()
                    }

                    override fun granted() {
                        activity?.let {
                            Intent(it, ScanActivity::class.java).apply {
                                // 先暂时从这里控制submit与否
                                putExtra(Const.SCAN_ENTER_FLAG, Const.SCAN_ENTER_SUBMIT)
                                startActivity(this)
                                it.overridePendingTransition(R.anim.slide_right_in, R.anim.scale_out)
                            }
                        }
                    }
                })
        }
        mHomeController.loadData()
    }

    fun onRefresh() {
        // TODO 刷新操作
    }

    override fun onDestroy() {
        mHomeController.onDestroy()
        PermissionUtils.getInstance().destroy()
        super.onDestroy()
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