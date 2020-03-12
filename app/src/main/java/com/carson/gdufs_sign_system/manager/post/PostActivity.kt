package com.carson.gdufs_sign_system.manager.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.utils.StatusBarUtil

class PostActivity : BaseActivity() {
    override fun getContentViewResId(): Int = R.layout.activity_post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
        StatusBarUtil.setStatusBarDarkTheme(this, false)
    }

    override fun onBackPressed() {
        finish(PostActivity::class.java.name)
        overridePendingTransition(R.anim.scale_in, R.anim.slide_right_out)
    }
}
