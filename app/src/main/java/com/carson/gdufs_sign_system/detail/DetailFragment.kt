package com.carson.gdufs_sign_system.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.detail.controller.DetailController
import com.carson.gdufs_sign_system.widget.RoundImageView

class DetailFragment private constructor(): BaseFragment(), IViewCallback {
    override fun onFabShow(value: Float) {
        Log.i(TAG, "value = $value")
        if (value > 0.1F) {
            mFloatingButton.visibility = View.VISIBLE
            mFloatingButton.translationY = 50F * (1F - value)
            mFloatingButton.alpha = value
        } else {
            mFloatingButton.visibility = View.GONE
        }
    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

    private lateinit var mRootView: View
    private lateinit var mBack: ImageView
    private lateinit var mScrollView: NestedScrollView
    private lateinit var mTitle: TextView
    private lateinit var mCover: RoundImageView
    private lateinit var mDetailInfo: TextView
    private lateinit var mDetailType: TextView
    private lateinit var mDetailTime: TextView
    private lateinit var mDetailArea: TextView
    private lateinit var mDetailSignedPeople: TextView
    private lateinit var mDetailEndTime: TextView
    private lateinit var mFloatingButton: RelativeLayout

    private lateinit var mDetailController: DetailController

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_detail, container, false)
        initViews()
        return mRootView
    }

    private fun initViews() {
        mDetailController = DetailController(this, this)

        mBack = mRootView.findViewById(R.id.detail_back)
        mScrollView = mRootView.findViewById(R.id.detail_scrollView)
        mTitle = mRootView.findViewById(R.id.detail_activity_name)
        mCover = mRootView.findViewById(R.id.detail_cover)
        mDetailInfo = mRootView.findViewById(R.id.detail_info)
        mDetailType = mRootView.findViewById(R.id.detail_type)
        mDetailTime = mRootView.findViewById(R.id.detail_time)
        mDetailArea = mRootView.findViewById(R.id.detail_area)
        mDetailSignedPeople = mRootView.findViewById(R.id.detail_people)
        mDetailEndTime = mRootView.findViewById(R.id.detail_end_time)
        mFloatingButton = mRootView.findViewById(R.id.detail_sign_fab)

        Glide.with(this).load("https://file.ourbeibei.com/l_e/static/mini_program_icons/banner_challenge.png").into(mCover)

        mScrollView.setOnScrollChangeListener(mDetailController)
        mBack.setOnClickListener(mDetailController)
        mFloatingButton.setOnClickListener(mDetailController)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDetailController.onDestroy()
    }

    companion object {

        private const val TAG = "DetailFragment"

        private const val FRAGMENT_TAG = "Detail"

        @JvmStatic
        fun newInstance() = DetailFragment().apply {
        }
    }

}