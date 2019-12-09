package com.carson.gdufs_sign_system.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.widget.RoundImageView

class DetailFragment private constructor(): BaseFragment() {
    override fun fragmentString(): String {
        return "Detail"
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
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailFragment().apply {
        }
    }

}