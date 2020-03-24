package com.carson.gdufs_sign_system.manager.post.controller

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carson.gdufs_sign_system.BuildConfig
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.manager.post.IViewCallback
import com.carson.gdufs_sign_system.manager.post.PostActivity
import com.carson.gdufs_sign_system.manager.post.SearchLocationActivity
import com.carson.gdufs_sign_system.manager.post.adapter.PopupMultiItem
import com.carson.gdufs_sign_system.manager.post.adapter.PopupMultiItemAdapter
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.carson.gdufs_sign_system.utils.ScreenUtils
import com.carson.gdufs_sign_system.widget.PickerPopupWindow
import com.carson.gdufs_sign_system.widget.PickerScrollView
import com.carson.gdufs_sign_system.widget.TimePickerView
import com.tencent.map.geolocation.*
import com.tencent.mapsdk.raster.model.*
import com.tencent.tencentmap.mapsdk.map.CameraUpdateFactory
import com.tencent.tencentmap.mapsdk.map.MapView
import org.w3c.dom.Text
import java.lang.ref.WeakReference

class PostController(private val context: WeakReference<Context>, private val mIView: IViewCallback) : TencentLocationListener {

    companion object {
        private const val TAG = "PostController"
    }

    private lateinit var mMapView: MapView

    private var mPopupWindow: PopupWindow? = null

    private var mPopupMultiChoiceWindow: PopupWindow? = null

    private var mPopupChoiceWindow: PickerPopupWindow? = null

    private var mLatLng: LatLng? = null

    private var mRadius = 0

    private var mCircle: Circle? = null

    fun setupMap(mMapView: MapView) {
        this.mMapView = mMapView
    }

    private fun initMapEvent(location: TencentLocation) {
        mMapView ?: return

        val mMap = mMapView.map
        val uiSettings = mMapView.uiSettings
        uiSettings.setScrollGesturesEnabled(false)
        uiSettings.setZoomGesturesEnabled(false)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng))
        mMap.setZoom(15)
        mMap.addMarker(MarkerOptions()).apply {
            setIcon(BitmapDescriptorFactory.fromResource(R.drawable.signin_location))
            title = location.address
            position = mLatLng
            setAnchor(0.5F, 0.5F)
        }

    }

    fun animateMap(data: Intent) {
        val mMap = mMapView.map

        val latLng = LatLng(data.getDoubleExtra("lat", mLatLng?.latitude!!),
            data.getDoubleExtra("lng", mLatLng?.longitude!!))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.addMarker(MarkerOptions()).apply {
            setIcon(BitmapDescriptorFactory.fromResource(R.drawable.signin_location))
            title = data.getStringExtra("address")
            position = latLng
            setAnchor(0.5F, 0.5F)
        }

        if (mRadius != 0 && mCircle != null) {
            Log.e(TAG, "add circle after location changed")
            mCircle?.remove()
            mCircle = mMap.addCircle(CircleOptions()).apply {
                center = latLng
                radius = mRadius.toDouble()
                strokeColor = context.get()?.resources?.getColor(R.color.colorCyan) ?: Color.CYAN
                strokeWidth = 5F
            }
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
                initMapEvent(it)
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

    private fun addCircle() {
        mMapView?: return
        val map = mMapView.map

        if (mCircle != null) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "addCircle circle is not null")
            }
            mCircle?.remove()
        }

        if (BuildConfig.DEBUG) {
            Log.e(TAG, "addCircle")
        }

        mCircle = map.addCircle(CircleOptions()).apply {
            center = mLatLng
            radius = mRadius.toDouble()
            strokeWidth = 5F
            strokeColor = context.get()?.resources?.getColor(R.color.colorCyan) ?: Color.CYAN
        }

    }

    fun initPopupChoicePickerWindow(anchorView: ViewGroup) {
        if (mPopupChoiceWindow == null) {
            mPopupChoiceWindow = PickerPopupWindow(
                context, anchorView, mutableListOf("300", "500", "800", "1000"), object : PickerPopupWindow.OnConfirmPickListener {
                    override fun onConfirmPick(value: String) {
                        mRadius = value.toInt()
                        context.get()?.apply {
                            mIView.onShowSelectedText(resources.getString(R.string.distance, value))
                        }
                        addCircle()
                    }
                }
            )
        }
        mPopupChoiceWindow?.show()
    }

    fun initPopupTimePickerWindow(anchorView: ViewGroup) {
        if (mPopupWindow == null) {
            val contentView = LayoutInflater.from(context.get()).inflate(R.layout.layout_popup_time_picker,
                anchorView, false)
            val timePicker = contentView.findViewById<TimePickerView>(R.id.popup_time_picker)
            mPopupWindow = PopupWindow(contentView).apply {
                width = ScreenUtils.getScreenWidth(context.get()!!)
                height = ScreenUtils.dip2px(context.get()!!, 260F)
                elevation = ScreenUtils.dip2px_5(context.get()!!).toFloat()
                animationStyle = R.style.PopupAnimation
                setBackgroundDrawable(ColorDrawable(Color.WHITE))
                isOutsideTouchable = true
            }
            contentView.findViewById<TextView>(R.id.popup_confirm).setOnClickListener {
                mIView.onShowSelectedText(timePicker.getResult())
                mPopupWindow?.dismiss()
            }
        }
        mPopupWindow?.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0)
    }

    fun initPopupMultiChoiceWindow(anchorView: ViewGroup) {
        if (mPopupMultiChoiceWindow == null) {
            val contentView = LayoutInflater.from(context.get()).inflate(R.layout.layout_popup_recyclerview,
                anchorView, false)
            val recyclerView = contentView.findViewById<RecyclerView>(R.id.popup_recyclerview)
            mPopupMultiChoiceWindow = PopupWindow(contentView).apply {
                width = ScreenUtils.getScreenWidth(context.get()!!)
                height = ScreenUtils.dip2px(context.get()!!, 250F)
                elevation = ScreenUtils.dip2px_5(context.get()!!).toFloat()
                animationStyle = R.style.PopupAnimation
                setBackgroundDrawable(ColorDrawable(Color.WHITE))
                isOutsideTouchable = true
            }
            // init recyclerview
            val dataList = mutableListOf<PopupMultiItem>()
            val clazzArray = context.get()!!.resources
                .getStringArray(R.array.static_class_name)
            clazzArray.forEach {
                dataList.add(PopupMultiItem(it, false))
            }
            val adapter = PopupMultiItemAdapter(dataList)
            recyclerView.adapter = adapter
            val gridLayoutManager = GridLayoutManager(context.get()!!,
                ScreenUtils.getScreenWidth(context.get()!!))
            val mPaint = Paint()
            mPaint.textSize = ScreenUtils.dip2px(context.get()!!, 12F).toFloat()
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val textWidth = mPaint.measureText(clazzArray[position]) + ScreenUtils.dip2px(context.get()!!, 48F)
                    return if (textWidth > ScreenUtils.getScreenWidth(context.get()!!))
                        ScreenUtils.getScreenWidth(context.get()!!) else textWidth.toInt()
                }
            }
            recyclerView.layoutManager = gridLayoutManager

            // init widget
            contentView.findViewById<TextView>(R.id.popup_confirm).setOnClickListener {
                mIView.onShowSelectedText(adapter.getResult())
                mPopupMultiChoiceWindow?.dismiss()
            }
        }
        mPopupMultiChoiceWindow?.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0)
    }

    fun navigateToPickLocation() {
        val toChoosePlace = Intent(context.get(), SearchLocationActivity::class.java).apply {
            putExtra("lat", mLatLng?.latitude)
            putExtra("lng", mLatLng?.longitude)
        }
        (context.get() as PostActivity?)?.apply {
            startActivityForResult(toChoosePlace, PostActivity.REQUEST_TO_SEARCH)
            overridePendingTransition(R.anim.slide_right_in, R.anim.scale_out)
        }
    }

}
