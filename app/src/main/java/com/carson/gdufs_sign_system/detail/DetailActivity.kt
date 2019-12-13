package com.carson.gdufs_sign_system.detail

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.carson.gdufs_sign_system.widget.RoundImageView

class DetailActivity : BaseActivity() {

    private var mDetailFragment: DetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (mDetailFragment == null) {
                mDetailFragment = DetailFragment.newInstance()
            }
            mDetailFragment?.let {
                it.allowReturnTransitionOverlap = true
                it.allowEnterTransitionOverlap = true
                supportFragmentManager.beginTransaction()
                    .add(R.id.detail_container, it, it.fragmentString()).commit()
            }
        }
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
        StatusBarUtil.setStatusBarDarkTheme(this, false)
    }

    override fun onBackPressed() {
        finish(DetailActivity::class.java.name)
        overridePendingTransition(R.anim.scale_in, R.anim.slide_right_out)
    }

    override fun getContentViewResId(): Int {
        return R.layout.activity_detail
    }

}