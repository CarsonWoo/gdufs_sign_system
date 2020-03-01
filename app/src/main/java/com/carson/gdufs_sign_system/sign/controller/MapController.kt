package com.carson.gdufs_sign_system.sign.controller

import android.graphics.Color
import android.util.Log
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.sign.MapFragment
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import com.tencent.mapsdk.raster.model.*
import com.tencent.tencentmap.mapsdk.map.MapView
import com.tencent.tencentmap.mapsdk.map.UiSettings

class MapController(mapFragment: MapFragment) : BaseController<MapFragment>(mapFragment), TencentLocationListener {

    companion object {
        private const val TAG = "MapController"
    }

    private var mMapView: MapView? = null
    private var mLatLng: LatLng? = null

    fun setMapView(mapView: MapView) {
        this.mMapView = mapView
    }

    fun initMapEvent() {
        mMapView ?: return

//        // 注册定位
//        registerLocationEvent()

        val mMap = mMapView?.map
        mMap?.isSatelliteEnabled = true
        mMap?.isTrafficEnabled = true
//        val guangzhou = mLatLng
//        mMap?.setCenter(guangzhou)
        mMap?.setZoom(16)

        // uiSettings
        val uiSettings = mMapView?.uiSettings
        uiSettings?.setLogoPosition(UiSettings.LOGO_POSITION_CENTER_BOTTOM)
        uiSettings?.setZoomGesturesEnabled(true)


    }

    fun registerLocationEvent() {
        val locationReq = TencentLocationRequest.create()
        locationReq.interval = 10 * 1000
        locationReq.requestLevel = TencentLocationRequest.REQUEST_LEVEL_NAME
        locationReq.isAllowCache = true
        val locationMgr = TencentLocationManager.getInstance(mFragment?.context)
        val err = locationMgr.requestLocationUpdates(locationReq, this)
        Log.i(TAG, "location errCode = $err")
    }

    override fun onStatusUpdate(name: String?, status: Int, desc: String?) {
        Log.i(TAG, "status : $desc")
        if (status == 2 || status == 5) {
            // STATUS = STATUS_DENIED(2) 定位权限被禁用 / STATUS = STATUS_LOCATION_SWITCH_OFF WIFI扫描被禁用
            // 都需要提示用户打开
            mFragment?.apply {
                PermissionUtils.getInstance().with(this).showDialog()
            }
        }
    }

    override fun onLocationChanged(location: TencentLocation?, err: Int, reason: String?) {
        if (TencentLocation.ERROR_OK == err) {
            // 定位成功
            Log.i(TAG, "location = ${location?.address}")
            refreshMap(location)
        } else {
            // 定位失败
            Log.i(TAG, "locate failed reason = $reason")
        }
        // 无论成功或失败 都remove?
        Log.i(TAG, "start remove listener")
        TencentLocationManager.getInstance(mFragment?.context).removeUpdates(this@MapController)
    }

    private fun refreshMap(location: TencentLocation?) {
        location?.apply {
            mLatLng = LatLng(latitude, longitude)
            val mMap = mMapView?.map
            val marker = mMap?.addMarker(MarkerOptions())
            marker?.apply {
                position = mLatLng
                title = address
                setAnchor(0.5f, 0.5f)
                setIcon(BitmapDescriptorFactory.defaultMarker())
                showInfoWindow()
            }

            // add circle
            val circle = mMap?.addCircle(CircleOptions())
            circle?.apply {
                center = mLatLng
                radius = 500.0
                fillColor = mFragment?.resources?.getColor(R.color.alphaColorCyan)?: Color.CYAN
                strokeColor = Color.parseColor("#7FFFFFFF")
                strokeWidth = 2F
            }
        }
    }

}