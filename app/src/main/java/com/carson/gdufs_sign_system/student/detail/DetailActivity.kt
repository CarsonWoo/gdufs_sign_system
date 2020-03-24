package com.carson.gdufs_sign_system.student.detail

import android.os.Bundle
import android.widget.FrameLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.StatusBarUtil

class DetailActivity : BaseActivity() {

    private var mDetailFragment: DetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        if (savedInstanceState == null) {
            if (mDetailFragment == null) {
                mDetailFragment = DetailFragment.newInstance().apply {
                    val bundle = Bundle()
                    bundle.putLong(Const.BundleKeys.DETAIL_ID,
                        intent.getLongExtra(Const.BundleKeys.DETAIL_ID, 0L))
                    arguments = bundle
                }
            }
            mDetailFragment?.let {
                it.allowReturnTransitionOverlap = true
                it.allowEnterTransitionOverlap = true
                supportFragmentManager.beginTransaction()
                    .add(R.id.detail_container, it, it.fragmentString()).commit()
            }
        }
        val params = findViewById<FrameLayout>(R.id.detail_container).layoutParams as FrameLayout.LayoutParams
        params.topMargin = StatusBarUtil.getStatusBarHeight(this)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.getInstance().with(this).onRequestPermissionResult(requestCode, permissions, grantResults)
    }

}