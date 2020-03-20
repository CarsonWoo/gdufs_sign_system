package com.carson.gdufs_sign_system.manager.post

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.manager.post.controller.SearchLocationController
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.tencent.mapsdk.raster.model.LatLng
import java.lang.ref.WeakReference

class SearchLocationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mWebView: WebView

    private lateinit var mBackView: ImageView

    private lateinit var mController: SearchLocationController

    private lateinit var mConfirmView: ImageView

    private var mLatLng: LatLng? = null

    override fun getContentViewResId(): Int {
        return R.layout.activity_search_location
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())

        initPresetData()

        initViews()

        initEvents()

        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        StatusBarUtil.setStatusBarDarkTheme(this, false)
    }

    private fun initPresetData() {
        mLatLng = LatLng(intent.getDoubleExtra("lat", Const.GDUFS_LATLNG.latitude),
            intent.getDoubleExtra("lng", Const.GDUFS_LATLNG.longitude))
    }

    private fun initViews() {
        mWebView = findViewById(R.id.search_web_view)
        mConfirmView = findViewById(R.id.search_confirm)
        mBackView = findViewById(R.id.search_back)
        mController = SearchLocationController(WeakReference(this))
    }

    private fun initEvents() {
        mLatLng?.let {
            mController.setUpWebView(mWebView, it)
        }
        mBackView.setOnClickListener(this)
        mConfirmView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_back -> {
                onBackPressed()
            }
            R.id.search_confirm -> {
                mController.navigateBackWithData(this)
            }
        }
    }

    override fun onBackPressed() {
        finish(SearchLocationActivity::class.java.name)
        overridePendingTransition(R.anim.scale_in, R.anim.slide_right_out)
    }
}
