package com.carson.gdufs_sign_system.main

import android.os.Bundle
import android.widget.FrameLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.base.LifeCallbackManager
import com.carson.gdufs_sign_system.widget.TabSelector

class HomeActivity: BaseActivity() {

    private lateinit var mContainer: FrameLayout
    private lateinit var mTabSelector: TabSelector
    private var mHomeController: HomeController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContainer = findViewById(R.id.home_container)
        mTabSelector = findViewById(R.id.tab_selector)
        mHomeController = HomeController(this)
        mHomeController?.initView()
        mHomeController?.let {
            mTabSelector.setSelectListener(it)
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()

        if (LifeCallbackManager.get().getActivitySize() == 1) {
            super.onBackPressed()
        } else {
            finish(HomeActivity::class.java.name)
            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHomeController?.onDestroy()
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_home
    }

}