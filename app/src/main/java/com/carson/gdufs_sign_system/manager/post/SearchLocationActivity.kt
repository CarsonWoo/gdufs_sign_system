package com.carson.gdufs_sign_system.manager.post

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.manager.post.controller.SearchLocationController
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import java.lang.ref.WeakReference

class SearchLocationActivity : BaseActivity() {

    private lateinit var mWebView: WebView

    private lateinit var mBackView: ImageView

    private lateinit var mController: SearchLocationController

    override fun getContentViewResId(): Int {
        return R.layout.activity_search_location
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())

        initViews()

        StatusBarUtil.setStatusBarColor(this, Color.TRANSPARENT)
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
    }

    private fun initViews() {
        mWebView = findViewById(R.id.search_web_view)
        mBackView = findViewById(R.id.search_back)
        mController = SearchLocationController(WeakReference(this))
        mController.setUpWebView(mWebView)

    }
}
