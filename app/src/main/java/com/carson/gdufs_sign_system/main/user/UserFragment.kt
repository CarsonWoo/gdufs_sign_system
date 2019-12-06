package com.carson.gdufs_sign_system.main.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.main.adapter.UserSignItemAdapter
import com.carson.gdufs_sign_system.main.model.SignItem
import com.carson.gdufs_sign_system.widget.CircleImageView

class UserFragment private constructor(): BaseFragment() {

    private lateinit var mRoot: View
    private lateinit var mAvatarView: CircleImageView
    private lateinit var mUsername: TextView
    private lateinit var mEditInfo: TextView
    private lateinit var mStudentId: TextView
    private lateinit var mClassId: TextView
    private lateinit var mActivityNum: TextView
    private lateinit var mRecyclerview: RecyclerView
    private lateinit var mDarkMode: RelativeLayout
    private lateinit var mScan: RelativeLayout
    private lateinit var mSetting: RelativeLayout

    private lateinit var mAdapter: UserSignItemAdapter

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_user, container, false)
        initViews()
        return mRoot
    }

    private fun initViews() {
        mAvatarView = mRoot.findViewById(R.id.avatar)
        mUsername = mRoot.findViewById(R.id.username)
        mEditInfo = mRoot.findViewById(R.id.edit_info)
        mStudentId = mRoot.findViewById(R.id.student_id)
        mClassId = mRoot.findViewById(R.id.student_class)
        mActivityNum = mRoot.findViewById(R.id.activity_num)
        mRecyclerview = mRoot.findViewById(R.id.user_recyclerview)
        mDarkMode = mRoot.findViewById(R.id.dark_mode)
        mScan = mRoot.findViewById(R.id.scan)
        mSetting = mRoot.findViewById(R.id.setting)

        context?.let {
            Glide.with(it).load("https://file.ourbeibei.com/l_e/user/portrait/002bd4ec-438e-4e8a-a07d-7527c29b10fc.jpg").into(mAvatarView)
        }

        // recyclerview
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
        mRecyclerview.adapter = mAdapter
        mRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerview.setHasFixedSize(true)
    }

    fun onRefresh() {
        // TODO 刷新操作
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mInstance: UserFragment? = null

        fun newInstance(): UserFragment {
            if (mInstance == null) mInstance = UserFragment()
            return mInstance!!
        }
    }

}