package com.carson.gdufs_sign_system.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.widget.CircleImageView

class UserFragment private constructor(): BaseFragment() {

    private lateinit var mRoot: View
    private lateinit var mAvatarView: CircleImageView

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
        context?.let {
            Glide.with(it).load("https://file.ourbeibei.com/l_e/user/portrait/002bd4ec-438e-4e8a-a07d-7527c29b10fc.jpg").into(mAvatarView)
        }
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