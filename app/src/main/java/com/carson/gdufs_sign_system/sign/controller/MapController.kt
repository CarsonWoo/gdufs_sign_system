package com.carson.gdufs_sign_system.sign.controller

import android.graphics.Color
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.sign.MapFragment
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.mapsdk.raster.model.*
import com.tencent.tencentmap.mapsdk.map.MapView
import com.tencent.tencentmap.mapsdk.map.UiSettings

class MapController(mapFragment: MapFragment) : BaseController<MapFragment>(mapFragment), TencentLocationListener {

    private var mMapView: MapView? = null

    fun setMapView(mapView: MapView) {
        this.mMapView = mapView
    }

    fun initMapEvent() {
        mMapView ?: return
        val mMap = mMapView?.map
        mMap?.isSatelliteEnabled = true
        mMap?.isTrafficEnabled = true
        val guangzhou = LatLng(23.0, 113.2)
        mMap?.setCenter(guangzhou)
        mMap?.setZoom(16)

        // uiSettings
        val uiSettings = mMapView?.uiSettings
        uiSettings?.setLogoPosition(UiSettings.LOGO_POSITION_CENTER_BOTTOM)
        uiSettings?.setZoomGesturesEnabled(true)

        val marker = mMap?.addMarker(MarkerOptions())
        marker?.apply {
            position = guangzhou
            title = "广州"
            setAnchor(0.5f, 0.5f)
            setIcon(BitmapDescriptorFactory.defaultMarker())
            showInfoWindow()
        }

        // add circle
        val circle = mMap?.addCircle(CircleOptions())
        circle?.apply {
            center = guangzhou
            radius = 500.0
            fillColor = mFragment?.resources?.getColor(R.color.alphaColorCyan)?: Color.CYAN
            strokeColor = Color.parseColor("#7FFFFFFF")
            strokeWidth = 2F
        }


    }

    override fun onStatusUpdate(p0: String?, p1: Int, p2: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(p0: TencentLocation?, p1: Int, p2: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}