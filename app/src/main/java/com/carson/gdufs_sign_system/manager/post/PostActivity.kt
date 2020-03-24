package com.carson.gdufs_sign_system.manager.post

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseActivity
import com.carson.gdufs_sign_system.manager.post.controller.PostController
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.tencent.tencentmap.mapsdk.map.MapView
import java.lang.ref.WeakReference

class PostActivity : BaseActivity(), IViewCallback, View.OnClickListener {

    private enum class SELECTTYPE {
        START_TIME, END_TIME, CLAZZ, RADIUS
    }

    companion object {
        private const val TAG = "PostActivity"
        const val REQUEST_TO_SEARCH = 0x1001
    }

    private lateinit var mBackView: ImageView
    private lateinit var mPostView: ImageView
    private lateinit var mEtActivityName: EditText
    private lateinit var mEtStartTime: EditText
    private lateinit var mEtEndTime: EditText
    private lateinit var mSignClazzLayout: LinearLayout
    private lateinit var mSignClazz: TextView
    private lateinit var mSignPlace: TextView
    private lateinit var mSignRadius: TextView
    private lateinit var mMapView: MapView
    private lateinit var mBtnPost: Button

    private lateinit var mPostController: PostController

    private lateinit var mContainer: CoordinatorLayout

    private var mSelectType = SELECTTYPE.START_TIME

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
        mSignRadius = findViewById(R.id.sign_radius)
        mMapView = findViewById(R.id.post_map_view)
        mBtnPost = findViewById(R.id.btn_post)
        mContainer = findViewById(R.id.post_container)

        mPostController = PostController(WeakReference(this), this)

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
        mSignClazzLayout.setOnClickListener(this)
        mEtStartTime.setOnClickListener(this)
        mEtEndTime.setOnClickListener(this)
        mBackView.setOnClickListener(this)
        mBtnPost.setOnClickListener(this)
        mPostView.setOnClickListener(this)
        mSignPlace.setOnClickListener(this)
        mSignRadius.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.et_start_time -> {
                mSelectType = SELECTTYPE.START_TIME
                mPostController.initPopupTimePickerWindow(mContainer)
            }
            R.id.et_end_time -> {
                mSelectType = SELECTTYPE.END_TIME
                mPostController.initPopupTimePickerWindow(mContainer)
            }
            R.id.sign_student_clazz_layout -> {
                // popup the multi choice selector
                mSelectType = SELECTTYPE.CLAZZ
                mPostController.initPopupMultiChoiceWindow(mContainer)
            }
            R.id.post_back -> {
                onBackPressed()
            }
            R.id.top_post, R.id.btn_post -> {
                // post the sign activity
            }
            R.id.sign_place -> {
                mPostController.navigateToPickLocation()
            }
            R.id.sign_radius -> {
                mSelectType = SELECTTYPE.RADIUS
                mPostController.initPopupChoicePickerWindow(mContainer)
            }
        }
    }

    override fun onShowSelectedText(text: String) {
        when (mSelectType) {
            SELECTTYPE.START_TIME -> mEtStartTime.setText(text)
            SELECTTYPE.END_TIME -> mEtEndTime.setText(text)
            SELECTTYPE.CLAZZ -> mSignClazz.text = text
            SELECTTYPE.RADIUS -> mSignRadius.text = text
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TO_SEARCH && resultCode == Activity.RESULT_OK) {
            Log.e(TAG, "onBackData address=${data?.getStringExtra("address")}")
            // 设置签到位置
            data?.let {
                mPostController.animateMap(it)
                mSignPlace.text = it.getStringExtra("address")
            }
        }
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
