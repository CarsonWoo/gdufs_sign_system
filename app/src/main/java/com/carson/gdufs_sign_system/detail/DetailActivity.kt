package com.carson.gdufs_sign_system.detail

import android.os.Bundle
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.utils.StatusBarUtil

class DetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
        StatusBarUtil.setStatusBarDarkTheme(this, false)
    }

    override fun onBackPressed() {
        finish(DetailActivity::class.java.name)
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_detail
    }

}