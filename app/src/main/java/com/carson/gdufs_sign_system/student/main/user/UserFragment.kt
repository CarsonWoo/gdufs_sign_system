package com.carson.gdufs_sign_system.student.main.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.login.LoginActivity
import com.carson.gdufs_sign_system.student.main.MainActivity
import com.carson.gdufs_sign_system.student.main.adapter.UserSignItemAdapter
import com.carson.gdufs_sign_system.student.main.controller.UserController
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.widget.RoundImageView
import java.lang.ref.WeakReference

class UserFragment: BaseFragment(), IViewCallback {
    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

    private lateinit var mRoot: View
    private lateinit var mAvatarView: RoundImageView
    private lateinit var mUsername: TextView
    private lateinit var mEditInfo: TextView
    private lateinit var mStudentId: TextView
    private lateinit var mClassId: TextView
    private lateinit var mActivityNum: TextView
    private lateinit var mRecyclerview: RecyclerView
    private lateinit var mDarkMode: RelativeLayout
    private lateinit var mScan: RelativeLayout
    private lateinit var mSetting: RelativeLayout
    private lateinit var mLogout: RelativeLayout

    private lateinit var mAdapter: UserSignItemAdapter

    private lateinit var mUserController: UserController

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_user, container, false)
        initViews()
        initEvents()
        return mRoot
    }

    private fun initViews() {
        mUserController = UserController(this, this)
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
        mLogout = mRoot.findViewById(R.id.logout)

        // recyclerview
        mAdapter = mUserController.getUserItemAdapter()
        mRecyclerview.adapter = mAdapter
        mRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerview.setHasFixedSize(true)
    }

    private fun initEvents() {
        mLogout.setOnClickListener {
            Const.getSharedPreference(WeakReference(context))?.edit()?.clear()?.apply()
            Const.removePrefKey()
            Intent(activity, LoginActivity::class.java).apply {
                startActivity(this)
                (activity as BaseActivity?)?.finish(MainActivity::class.java.name)
            }
        }
        mUserController.loadPersonalData()
    }

    fun onRefresh() {
        mUserController.loadPersonalData()
    }

    override fun onDataLoaded(
        username: String,
        studentId: String,
        clazz: String,
        signedNum: String
    ) {
        mActivityNum.text = signedNum
        mUsername.text = username
        mStudentId.text = studentId
        mClassId.text = clazz
    }

    override fun onDestroy() {
        super.onDestroy()
        mUserController.onDestroy()
    }

    companion object {
        private const val FRAGMENT_TAG = "User"
        @JvmStatic
        fun newInstance() = UserFragment().apply {
        }
    }

}