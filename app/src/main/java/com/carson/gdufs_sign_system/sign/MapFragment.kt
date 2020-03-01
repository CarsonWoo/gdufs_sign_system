package com.carson.gdufs_sign_system.sign

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseFragment
import com.carson.gdufs_sign_system.base.BaseFragmentActivity
import com.carson.gdufs_sign_system.sign.controller.MapController
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.tencent.tencentmap.mapsdk.map.MapView
import com.tencent.tencentmap.mapsdk.map.TencentMap

class MapFragment : BaseFragment(), IViewCallback {
    override fun onFabShow() {
        mSignButton.setBackgroundResource(R.drawable.button_solid_cyan_style)
        mSignButton.isEnabled = true
        mSignButton.isClickable = true
    }

    companion object {
        private const val FRAGMENT_TAG = "Map"
        fun newInstance() = MapFragment().apply {

        }
    }

    private lateinit var mRoot: View
    private var mMapView: MapView? = null
    private lateinit var mBack: ImageView
    private lateinit var mBackLayout: LinearLayout
    private lateinit var mSignButton: Button
    private lateinit var mLocateButton: ImageView

    private lateinit var mMapController: MapController

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_map, container, false)
        PermissionUtils.getInstance().with(this).permissions(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE)
            .requestCode(PermissionUtils.CODE_MULTI).request(object: PermissionUtils.PermissionCallback {
                override fun denied() {
                    PermissionUtils.getInstance().showDialog()
                }

                override fun granted() {
                    initViews()
                    initEvents()
                }
            })
        return mRoot
    }

    /**
     * mapView的生命周期管理 start
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMapView?.onCreate(savedInstanceState)
        activity?.apply {
            StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.transparent))
            StatusBarUtil.setStatusBarDarkTheme(this, false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView?.onSaveInstanceState(outState)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        activity?.apply {
            StatusBarUtil.setStatusBarColor(this, if (!hidden) resources.getColor(R.color.colorCyan) else resources.getColor(R.color.colorWhite))
            StatusBarUtil.setStatusBarDarkTheme(this, hidden)
        }
    }

    override fun onResume() {
        mMapView?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mMapView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        mMapView?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mMapView?.onDestroy()
        super.onDestroy()
    }

    /**
     * mapView的生命周期管理 end
     */

    private fun initViews() {
        mMapController = MapController(this, this)
        mMapView = mRoot.findViewById(R.id.map_view)
        mBack = mRoot.findViewById(R.id.map_back)
        mBackLayout = mRoot.findViewById(R.id.map_back_layout)
        mSignButton = mRoot.findViewById(R.id.btn_sign_scan)
        mLocateButton = mRoot.findViewById(R.id.btn_locate)
        mMapView.let {
            if (it != null) {
                mMapController.setMapView(it)
                mMapController.registerLocationEvent()
                mMapController.initMapEvent()
            }
        }
    }

    private fun initEvents() {
        mSignButton.setOnClickListener {
            (activity as BaseFragmentActivity?)?.apply {
                setFragmentAnimation(R.anim.slide_right_in, R.anim.slide_left_out)
                hide(this@MapFragment)
                show("SignSuccess")
            }
        }
        mLocateButton.setOnClickListener {
            mMapController.registerLocationEvent()
        }
    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

}