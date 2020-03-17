package com.carson.gdufs_sign_system.manager.post.controller

import android.Manifest
import android.content.Context
import android.util.Log
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.tencent.map.geolocation.*
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory
import com.tencent.mapsdk.raster.model.LatLng
import com.tencent.mapsdk.raster.model.MarkerOptions
import com.tencent.tencentmap.mapsdk.map.CameraUpdateFactory
import com.tencent.tencentmap.mapsdk.map.MapView
import java.lang.ref.WeakReference

class PostController(private val context: WeakReference<Context>) : TencentLocationListener {

    companion object {
        private const val TAG = "PostController"
    }

    private lateinit var mMapView: MapView

    private var mLatLng: LatLng? = null

    fun setupMap(mMapView: MapView) {
        this.mMapView = mMapView
    }

    fun setMapEvent(location: TencentLocation) {
        mMapView ?: return

        val mMap = mMapView.map
        val uiSettings = mMapView.uiSettings
        uiSettings.setScrollGesturesEnabled(false)
        uiSettings.setZoomGesturesEnabled(false)

        val locationList = location.poiList

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng))
        mMap.setZoom(15)
        mMap.addMarker(MarkerOptions()).apply {
            setIcon(BitmapDescriptorFactory.fromResource(R.drawable.signin_location))
            title = location.address
            position = mLatLng
            setAnchor(0.5F, 0.5F)
        }

    }

    fun registerLocation() {
        val locationReq = TencentLocationRequest.create()
        locationReq.interval = 10 * 1000
        locationReq.requestLevel = TencentLocationRequest.REQUEST_LEVEL_NAME
        locationReq.isAllowCache = true
        val locationMgr = TencentLocationManager.getInstance(context.get())
        val err = locationMgr.requestLocationUpdates(locationReq, this)
    }

    override fun onLocationChanged(location: TencentLocation?, err: Int, reason: String?) {
        if (TencentLocation.ERROR_OK == err) {
            // 定位成功
            Log.i(TAG, "location = ${location?.address}")
//            refreshMap(location)
            location?.let {
                mLatLng = LatLng(it.latitude, it.longitude)
                setMapEvent(it)
            }
        } else {
            // 定位失败
            Log.i(TAG, "locate failed reason = $reason")
        }
        TencentLocationManager.getInstance(context.get()).removeUpdates(this)
    }

    override fun onStatusUpdate(name: String?, status: Int, desc: String?) {
        Log.i(TAG, "status : $desc & name = $name")
        if (status == 2 || status == 5) {
            // STATUS = STATUS_DENIED(2) 定位权限被禁用 / STATUS = STATUS_LOCATION_SWITCH_OFF WIFI扫描被禁用
            // 都需要提示用户打开
            // 这里再加一层判断 因为不知道发什么神经 回调会说location permission denied
            context.get()?.let {
                if (!PermissionUtils.isGranted(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        it
                    ) ||
                    !PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION, it)
                ) {
                    PermissionUtils.getInstance().showDialog()
                }

            }
        }
    }
}