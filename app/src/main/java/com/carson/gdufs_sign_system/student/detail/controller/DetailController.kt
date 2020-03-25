package com.carson.gdufs_sign_system.student.detail.controller

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import com.carson.gdufs_sign_system.R
import com.carson.gdufs_sign_system.base.BaseController
import com.carson.gdufs_sign_system.student.detail.DetailActivity
import com.carson.gdufs_sign_system.student.detail.DetailFragment
import com.carson.gdufs_sign_system.student.detail.IViewCallback
import com.carson.gdufs_sign_system.student.sign.SignActivity
import com.carson.gdufs_sign_system.utils.Const
import com.carson.gdufs_sign_system.utils.PermissionUtils
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory
import com.tencent.mapsdk.raster.model.CircleOptions
import com.tencent.mapsdk.raster.model.LatLng
import com.tencent.mapsdk.raster.model.MarkerOptions
import com.tencent.tencentmap.mapsdk.map.MapView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class DetailController(detailFragment: DetailFragment, private val mIView: IViewCallback) :
    BaseController<DetailFragment>(detailFragment),
    NestedScrollView.OnScrollChangeListener, View.OnClickListener, CoroutineScope {

    private var mJob = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    private lateinit var mLatLng: LatLng
    private var mRadius: Int = 500
    private var mTitle: String? = null

//    private var mMapView: MapView? = null

    fun setupMapView(mapView: MapView, lat: Double, lng: Double, radius: Int, title: String?) {
//        this.mMapView = mapView
        mLatLng = LatLng(lat, lng)
        mTitle = title
        mRadius = radius

        mapView.uiSettings.setZoomGesturesEnabled(false)
        mapView.uiSettings.setScrollGesturesEnabled(false)

        val map = mapView.map
        map.setZoom(15)
        map.setCenter(LatLng(lat, lng))
        map.addMarker(MarkerOptions()).apply {
            setIcon(BitmapDescriptorFactory.fromResource(R.drawable.signin_location))
            setAnchor(0.5F, 0.5F)
            position = LatLng(lat, lng)
        }
        map.addCircle(CircleOptions()).apply {
            center = LatLng(lat, lng)
            setRadius(radius.toDouble())
            strokeWidth = 5F
            strokeColor = mFragment?.resources?.getColor(R.color.colorCyan) ?: Color.CYAN
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.detail_back -> {
                (mFragment?.activity as DetailActivity?)?.onBackPressed()
            }
            R.id.detail_sign_fab -> {
                mFragment?.let {
                    PermissionUtils.getInstance().with(it).requestCode(PermissionUtils.CODE_MULTI)
                        .permissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                        .request(object : PermissionUtils.PermissionCallback {
                            override fun denied() {
                                PermissionUtils.getInstance().showDialog()
                            }

                            override fun granted() {
                                (mFragment?.activity as DetailActivity?)?.apply {
                                    val toSign = Intent(this,
                                        SignActivity::class.java).apply {
                                        putExtra(Const.BundleKeys.SIGN_LAT, mLatLng.latitude)
                                        putExtra(Const.BundleKeys.SIGN_LNG, mLatLng.longitude)
                                        putExtra(Const.BundleKeys.SIGN_PLACE, mTitle)
                                        putExtra(Const.BundleKeys.SIGN_RADIUS, mRadius)
                                        putExtra(Const.BundleKeys.DETAIL_ID, v.tag as Long)
                                    }
                                    startActivityForResult(toSign, Const.REQUEST_CODE_FROM_DETAIL_TO_SIGN)
                                    overridePendingTransition(
                                        R.anim.slide_right_in,
                                        R.anim.slide_left_out
                                    )
                                }
                            }
                        })
                }
            }
        }
    }

    companion object {
        private const val TAG = "DetailController"
        private const val SCROLL_BASE = 30F
    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        val scrollMax = v?.getChildAt(0)?.height?.minus(v.measuredHeight)
        if (scrollY >= SCROLL_BASE) {
            scrollMax?.apply {
                Log.i(TAG, "${(scrollY - SCROLL_BASE) / (this.toFloat() - SCROLL_BASE)}")
                mIView.onFabShow((scrollY - SCROLL_BASE) / (this.toFloat() - SCROLL_BASE))
            }
        }
    }

    fun loadData(id: Long) {
        val dataMap = hashMapOf(
            Pair(
                "username",
                Const.getSharedPreference(WeakReference(mFragment?.context))?.getString(
                    Const.PreferenceKeys.USER_ID,
                    ""
                )
            ),
            Pair("programId", id)
        )
        mJob.cancel()
        needGsonConverter(true)
        mJob = executeRequest(
            request = {
                Log.e(TAG, "request")
                mApiService.getDetailData(dataMap).execute()
            },
            onSuccess = { res ->
                Log.e(TAG, "res = ?")
                if (res.isSuccessful) {
                    Log.e(TAG, "res.body = ${res.body()}")
                    res.body()?.let {
                        mIView.onDataLoaded(it)
                    }
                } else {
                    Log.e(TAG, res.message())
                    mIView.onDataLoadFail(if (res.message().isEmpty()) Const.Net.ERR_MSG_COMMON
                        else res.message())
                }
            },
            onFail = {
                Log.e(TAG, it.message)
                mIView.onDataLoadFail(if (it.message?.isEmpty() == true) Const.Net.ERR_MSG_COMMON
                    else it.message)
            }
        )
    }

}