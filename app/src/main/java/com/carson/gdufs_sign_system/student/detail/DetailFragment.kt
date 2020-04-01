package com.carson.gdufs_sign_system.student.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.model.SignDetailBean
import com.carson.gdufs_sign_system.student.detail.controller.DetailController
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.widget.RoundImageView
import com.tencent.tencentmap.mapsdk.map.MapView

class DetailFragment : BaseFragment(), IViewCallback {

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

    private lateinit var mRootView: View
    private lateinit var mBack: ImageView
    private lateinit var mScrollView: NestedScrollView
    private lateinit var mTitle: TextView
    private lateinit var mCover: RoundImageView
    private lateinit var mMapView: MapView
    private lateinit var mDetailSignPlace: TextView
    private lateinit var mDetailType: TextView
    private lateinit var mDetailTime: TextView
    private lateinit var mDetailRadius: TextView
    private lateinit var mDetailSignedPeople: TextView
    private lateinit var mDetailEndTime: TextView
    private lateinit var mFloatingButton: RelativeLayout
    private lateinit var mDetailSign: TextView
    private lateinit var mDetailTypeImage: ImageView

    private lateinit var mProgressBar: ProgressBar

    private lateinit var mDetailController: DetailController

    private var mIsSigned = false

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_detail, container, false)
        initViews()
        initEvents()
        return mRootView
    }

    private fun initViews() {
        mDetailController = DetailController(this, this)

        mBack = mRootView.findViewById(R.id.detail_back)
        mScrollView = mRootView.findViewById(R.id.detail_scrollView)
        mTitle = mRootView.findViewById(R.id.detail_activity_name)
        mCover = mRootView.findViewById(R.id.detail_cover)
        mMapView = mRootView.findViewById(R.id.detail_map_view)
        mDetailSignPlace = mRootView.findViewById(R.id.detail_sign_place)
        mDetailType = mRootView.findViewById(R.id.detail_type)
        mDetailTime = mRootView.findViewById(R.id.detail_time)
        mDetailRadius = mRootView.findViewById(R.id.detail_radius)
        mDetailSignedPeople = mRootView.findViewById(R.id.detail_people)
        mDetailEndTime = mRootView.findViewById(R.id.detail_end_time)
        mFloatingButton = mRootView.findViewById(R.id.detail_sign_fab)
        mDetailTypeImage = mRootView.findViewById(R.id.detail_type_img)
        mProgressBar = mRootView.findViewById(R.id.detail_progress_bar)
        mDetailSign = mRootView.findViewById(R.id.detail_sign)

        mProgressBar.visibility = View.VISIBLE
        mFloatingButton.isEnabled = false
    }

    private fun initEvents() {
        mFloatingButton.tag = arguments?.getLong(Const.BundleKeys.DETAIL_ID)
        mScrollView.setOnScrollChangeListener(mDetailController)
        mBack.setOnClickListener(mDetailController)
        mFloatingButton.setOnClickListener(mDetailController)
        mDetailSign.setOnClickListener(mDetailController)

        arguments?.let {
            mDetailController.loadData(it.getLong(Const.BundleKeys.DETAIL_ID))
        }
    }

    override fun onFabShow(value: Float) {
        Log.i(TAG, "value = $value")
        if (value > 0.1F && !mIsSigned) {
            mFloatingButton.visibility = View.VISIBLE
            mFloatingButton.translationY = 50F * (1F - value)
            mFloatingButton.alpha = value
        } else {
            mFloatingButton.visibility = View.GONE
        }
    }

    override fun onDataLoaded(data: SignDetailBean) {
        mProgressBar.visibility = View.GONE
        mScrollView.visibility = View.VISIBLE
        mFloatingButton.isEnabled = true

        mTitle.text = data.programName
        Glide.with(this).load(data.picUrl).into(mCover)
        mIsSigned = data.status != "未签到"
        mDetailRadius.text = resources.getString(R.string.detail_sign_radius,
            resources.getString(R.string.distance, data.range.toString()))
        mDetailTime.text = resources.getString(R.string.detail_time, data.startTime)
        mDetailEndTime.text = resources.getString(R.string.detail_end_time, data.endTime)
        mDetailSignPlace.text = data.place
        mDetailType.text = resources.getString(R.string.detail_type, data.status)
        mDetailTypeImage.setImageResource(if (mIsSigned) R.drawable.status_pass else R.drawable.status_unpass)
        mDetailSignedPeople.text = resources.getString(R.string.detail_people, data.signedNum, data.totalNum)

        mDetailController.setupMapView(mMapView, data.latitude, data.longtitude, data.range, data.place)
    }

    override fun onDataLoadFail(errMsg: String?) {
        mProgressBar.visibility = View.GONE
        Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        PermissionUtils.getInstance().destroy()
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