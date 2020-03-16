package com.carson.gdufs_sign_system.manager.post

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.manager.post.controller.PostController
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.tencent.tencentmap.mapsdk.map.MapView
import java.lang.ref.WeakReference

class PostActivity : BaseActivity() {

    private lateinit var mBackView: ImageView
    private lateinit var mPostView: ImageView
    private lateinit var mEtActivityName: EditText
    private lateinit var mEtStartTime: EditText
    private lateinit var mEtEndTime: EditText
    private lateinit var mSignClazzLayout: LinearLayout
    private lateinit var mSignClazz: TextView
    private lateinit var mSignPlace: TextView
    private lateinit var mMapView: MapView
    private lateinit var mBtnPost: Button

    private lateinit var mPostController: PostController

    override fun getContentViewResId(): Int = R.layout.activity_post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
        StatusBarUtil.setStatusBarDarkTheme(this, false)

        initViews()
//        initEvents()
    }

    private fun initViews() {
        mBackView = findViewById(R.id.post_back)
        mPostView = findViewById(R.id.top_post)
        mEtActivityName = findViewById(R.id.et_activity_name)
        mEtStartTime = findViewById(R.id.et_start_time)
        mEtEndTime = findViewById(R.id.et_end_time)
        mSignClazzLayout = findViewById(R.id.sign_student_clazz_layout)
        mSignClazz = findViewById(R.id.sign_student_clazz)
        mSignPlace = findViewById(R.id.sign_place)
        mMapView = findViewById(R.id.post_map_view)
        mBtnPost = findViewById(R.id.btn_post)

        mPostController = PostController(WeakReference(this))

        PermissionUtils.getInstance().with(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .requestCode(PermissionUtils.CODE_MULTI)
            .request(object : PermissionUtils.PermissionCallback {
                override fun denied() {
                    PermissionUtils.getInstance().showDialog()
                }

                override fun granted() {
                    initEvents()
                }
            })
    }

    private fun initEvents() {
        mPostController.setupMap(mMapView)
        mPostController.registerLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        finish(PostActivity::class.java.name)
        overridePendingTransition(R.anim.scale_in, R.anim.slide_right_out)
    }

    override fun onDestroy() {
        PermissionUtils.getInstance().destroy()
        super.onDestroy()
    }
}
