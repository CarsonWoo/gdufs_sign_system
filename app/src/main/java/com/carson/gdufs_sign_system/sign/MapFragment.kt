package com.carson.gdufs_sign_system.sign

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
import com.carson.gdufs_sign_system.utils.StatusBarUtil
import com.tencent.tencentmap.mapsdk.map.MapView
import com.tencent.tencentmap.mapsdk.map.TencentMap

class MapFragment : BaseFragment() {

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

    override fun getContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(R.layout.fragment_map, container, false)
        initViews()
        initEvents()
        return mRoot
    }

    /**
     * mapView的生命周期管理 start
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMapView?.onCreate(savedInstanceState)
        activity?.apply {
            StatusBarUtil.setStatusBarColor(this, resources.getColor(R.color.colorCyan))
            StatusBarUtil.setStatusBarDarkTheme(this, false)
        }
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
        mMapView = mRoot.findViewById(R.id.map_view)
        mBack = mRoot.findViewById(R.id.map_back)
        mBackLayout = mRoot.findViewById(R.id.map_back_layout)
        mSignButton = mRoot.findViewById(R.id.btn_sign_scan)
    }

    private fun initEvents() {
        mSignButton.setOnClickListener {
            (activity as BaseFragmentActivity?)?.apply {
                setFragmentAnimation(R.anim.slide_right_in, R.anim.slide_left_out)
                hide(this@MapFragment)
                show("SignSuccess")
            }
        }
    }

    override fun fragmentString(): String {
        return FRAGMENT_TAG
    }

}