package com.carson.gdufs_sign_system.student.sign.controller

import android.Manifest
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import com.carson.gdufs_sign_system.BuildConfig
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.student.sign.IViewCallback
import com.carson.gdufs_sign_system.student.sign.MapFragment
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.tencent.map.geolocation.*
import com.tencent.mapsdk.raster.model.*
import com.tencent.tencentmap.mapsdk.map.CameraUpdateFactory
import com.tencent.tencentmap.mapsdk.map.MapView
import com.tencent.tencentmap.mapsdk.map.TencentMap
import com.tencent.tencentmap.mapsdk.map.UiSettings

class MapController(mapFragment: MapFragment, private val mView: IViewCallback) :
    BaseController<MapFragment>(mapFragment),
    TencentLocationListener, TencentMap.OnMapCameraChangeListener {

    companion object {
        private const val TAG = "MapController"

    }

    private var mMapView: MapView? = null
    private var mCircleView: Circle? = null
    private var mSchoolMarkerView: Marker? = null
    private var mLatLng: LatLng? = null
    private var mPositionMarker: Marker? = null

    // 23.114335 & 113.214641  我家定位
    private var mTargetLatLng = Const.GDUFS_LATLNG

    fun setMapView(mapView: MapView) {
        this.mMapView = mapView
    }

    fun initMapEvent(lat: Double, lng: Double, titleStr: String?, distanceRadius: Int) {
        mMapView ?: return

        mTargetLatLng = LatLng(lat, lng)

        val mMap = mMapView?.map
        mMap?.setOnMapCameraChangeListener(this)
//        mMap?.setCenter(Const.GDUFS_LATLNG)

        mMap?.setCenter(mTargetLatLng)
//        mMap?.setZoom(16)

        // uiSettings
        val uiSettings = mMapView?.uiSettings
        uiSettings?.setLogoPosition(UiSettings.LOGO_POSITION_CENTER_BOTTOM)
        uiSettings?.setZoomGesturesEnabled(true)

        mSchoolMarkerView = mMap?.addMarker(MarkerOptions())?.apply {
            position = mTargetLatLng
//            position = Const.GDUFS_LATLNG
            title = titleStr?: Const.GDUFS_STR
            setAnchor(0.5f, 0.5f)
            setIcon(BitmapDescriptorFactory.fromResource(R.drawable.signin_location))
            showInfoWindow()
        }

        // add circle
        mCircleView = mMap?.addCircle(CircleOptions())?.apply {
            center = mTargetLatLng
            //            center = Const.GDUFS_LATLNG
            radius = distanceRadius.toDouble()
            fillColor = mFragment?.resources?.getColor(R.color.alphaColorCyan)?: Color.CYAN
            strokeColor = Color.parseColor("#7FFFFFFF")
            strokeWidth = 2F
        }
    }

    // 开始定位
    fun registerLocationEvent() {
        val locationReq = TencentLocationRequest.create()
        locationReq.interval = 10 * 1000
        locationReq.requestLevel = TencentLocationRequest.REQUEST_LEVEL_NAME
        locationReq.isAllowCache = true
        val locationMgr = TencentLocationManager.getInstance(mFragment?.context)
        val err = locationMgr.requestLocationUpdates(locationReq, this)
        Log.i(TAG, "location errCode = $err")
    }

    // 定位的回调
    override fun onStatusUpdate(name: String?, status: Int, desc: String?) {
        Log.i(TAG, "status : $desc & name = $name")
        if (status == 2 || status == 5) {
            // STATUS = STATUS_DENIED(2) 定位权限被禁用 / STATUS = STATUS_LOCATION_SWITCH_OFF WIFI扫描被禁用
            // 都需要提示用户打开
            // 这里再加一层判断 因为不知道发什么神经 回调会说location permission denied
            mFragment?.let {
                if (!PermissionUtils.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION, it.context!!) ||
                    !PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION, it.context!!)) {
                    PermissionUtils.getInstance().with(it).showDialog()
                }
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
        // 无论成功或失败 都remove 否则会一直add
        Log.i(TAG, "start remove listener")
        TencentLocationManager.getInstance(mFragment?.context).removeUpdates(this@MapController)
    }

    override fun onCameraChangeFinish(position: CameraPosition?) {
        // 这里是地图移动完成以后的回调
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onCameraChangeFinish: position = ${position?.target?.latitude}," +
                    " ${position?.target?.longitude}")
        }
        if (mLatLng?.let { mCircleView?.contains(it) } == true) {
            // 如果在签到点中
//            mMapView?.map?.animateCamera(CameraUpdateFactory.newLatLngZoom(Const.GDUFS_LATLNG, 15F))
            mMapView?.map?.animateCamera(CameraUpdateFactory.newLatLngZoom(mTargetLatLng, 15F))
            mSchoolMarkerView?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.signin_location))
        } else {
            // 不在签到点 做些什么？
        }
    }

    override fun onCameraChange(p0: CameraPosition?) {

    }

    private fun refreshMap(location: TencentLocation?) {
        location?.apply {
            mLatLng = LatLng(latitude, longitude)
            mLatLng ?: return@apply
            val mMap = mMapView?.map

            if (mPositionMarker != null) {
                mPositionMarker?.remove()
            }

            mPositionMarker = mMap?.addMarker(MarkerOptions())
            mPositionMarker?.apply {
                position = mLatLng
                setAnchor(0.5f, 0.5f)
                setIcon(BitmapDescriptorFactory.fromView(View.inflate(mFragment?.context,
                    R.layout.layout_marker_my_location, null)))
            }

            if (mCircleView?.contains(mLatLng) == false) {
                // 超出签到距离
                mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(createBounds(mLatLng!!, mTargetLatLng), 10),
                    1000L, null)
                Toast.makeText(mFragment?.context, "超出签到距离", Toast.LENGTH_SHORT).show()
            } else {
                // 满足签到距离
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(mTargetLatLng, 15F))
                mView.onFabShow()
            }
        }
    }

    private fun createBounds(latLngA: LatLng, latLngB: LatLng): LatLngBounds {
        val topLat = if (latLngA.latitude >= latLngB.latitude) latLngA.latitude else latLngB.latitude
        val bottomLat = if (latLngA.latitude >= latLngB.latitude) latLngB.latitude else latLngA.latitude
        val topLng = if (latLngA.longitude >= latLngB.longitude) latLngA.longitude else latLngB.longitude
        val bottomLng = if (latLngA.longitude >= latLngB.longitude) latLngB.longitude else latLngA.longitude

        val northeastLatLng = LatLng(topLat, topLng)
        val southwestLatLng = LatLng(bottomLat, bottomLng)

        return LatLngBounds(southwestLatLng, northeastLatLng)
    }

}