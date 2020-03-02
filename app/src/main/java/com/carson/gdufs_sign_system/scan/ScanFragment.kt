package com.carson.gdufs_sign_system.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carson.gdufs_sign_system.base.BaseFragment

class ScanFragment: BaseFragment() {

    companion object {

        private const val TAG = "ScanFragment"

        private const val FRAGMENT_TAG = "Scan"

        fun newInstance() = ScanFragment().apply {

        }
    }

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

}