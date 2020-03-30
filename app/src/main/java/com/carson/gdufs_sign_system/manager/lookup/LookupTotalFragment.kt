package com.carson.gdufs_sign_system.manager.lookup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.manager.lookup.adapter.LookupTotalItemAdapter
import com.carson.gdufs_sign_system.manager.lookup.controller.LookupTotalController
import com.carson.gdufs_sign_system.utils.Const

class LookupTotalFragment : BaseFragment() {

    companion object {
        private const val FRAGMENT_TAG = "LookupTotal"

        fun newInstance(): LookupTotalFragment = LookupTotalFragment().apply {  }

    }

    private lateinit var mRoot: View
    private lateinit var mBackView: ImageView
    private lateinit var mTitleView: TextView
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: LookupTotalItemAdapter

    private lateinit var mLookupTotalController: LookupTotalController

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
        mLookupTotalController = LookupTotalController(this)

        mBackView = mRoot.findViewById(R.id.lookup_back)
        mTitleView = mRoot.findViewById(R.id.lookup_title)
        mRecyclerView = mRoot.findViewById(R.id.lookup_recyclerview)
        mTitleView.text = resources.getString(R.string.lookup_my_sign_activity)

        mRecyclerView.adapter = mLookupTotalController.getAdapter()
    }

    private fun initEvents() {
        mBackView.setOnClickListener {
            (activity as LookupActivity?)?.onBackPressed()
        }

        mLookupTotalController.loadData()
    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }
}