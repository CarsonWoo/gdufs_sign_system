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
import com.carson.gdufs_sign_system.manager.lookup.adapter.LookupSingleItemAdapter
import com.carson.gdufs_sign_system.manager.lookup.controller.LookupSingleController
import com.carson.gdufs_sign_system.manager.lookup.model.SingleItemModel

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

    private val mItemList = mutableListOf<SingleItemModel>()

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
        mBackView = mRoot.findViewById(R.id.lookup_back)
        mTitleView = mRoot.findViewById(R.id.lookup_title)
        mRecyclerView = mRoot.findViewById(R.id.lookup_recyclerview)
//        mTitleView.text = resources.getString(R.string.)

        mockData()

        mAdapter = LookupSingleItemAdapter(mItemList)

        mRecyclerView.adapter = mAdapter

        mLookupSingleController = LookupSingleController(this)
    }

    private fun initEvents() {
        mBackView.setOnClickListener {
            (activity as LookupActivity?)?.onBackPressed()
        }
    }

    private fun mockData() {
        (0..10).forEach {
            val model = SingleItemModel("userName $it",
                "软工1603班", it % 2 == 0)
            mItemList.add(model)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mRecyclerView.smoothScrollToPosition(0)
    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

}