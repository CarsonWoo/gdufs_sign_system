package com.carson.gdufs_sign_system.manager.lookup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.manager.lookup.adapter.LookupSingleItemAdapter
import com.carson.gdufs_sign_system.manager.lookup.controller.LookupSingleController
import com.carson.gdufs_sign_system.model.MyActivityStudentItemBean
import com.carson.gdufs_sign_system.utils.Const

class LookupSingleFragment: BaseFragment() {

    companion object {
        private const val FRAGMENT_TAG = "LookupSingle"

        fun newInstance(): LookupSingleFragment = LookupSingleFragment().apply {

        }
    }

    private lateinit var mRoot: View
    private lateinit var mBackView: ImageView
    private lateinit var mTitleView: TextView
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: LookupSingleItemAdapter

    private lateinit var mLookupSingleController: LookupSingleController

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = LayoutInflater.from(container?.context).inflate(R.layout.fragment_lookup, container, false)
        initViews()
        initEvents()
        return mRoot
    }

    private fun initViews() {
        mLookupSingleController = LookupSingleController(this)

        mBackView = mRoot.findViewById(R.id.lookup_back)
        mTitleView = mRoot.findViewById(R.id.lookup_title)
        mRecyclerView = mRoot.findViewById(R.id.lookup_recyclerview)

        mRecyclerView.adapter = mLookupSingleController.getAdapter()
    }

    private fun initEvents() {
        mBackView.setOnClickListener {
            (activity as LookupActivity?)?.onBackPressed()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            // 如果显示
            mRecyclerView.smoothScrollToPosition(0)
            // 当重新显示
            Log.e(FRAGMENT_TAG, "onResume")
            Log.e(FRAGMENT_TAG, "argument = ${arguments?.getLong(Const.BundleKeys.ACTIVITY_ID)}")
            if (arguments?.getBoolean(Const.BundleKeys.ACTIVITY_DETAIL_SHOULD_RELOAD) == true) {
                mLookupSingleController.loadData()
            }
        }
    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

}