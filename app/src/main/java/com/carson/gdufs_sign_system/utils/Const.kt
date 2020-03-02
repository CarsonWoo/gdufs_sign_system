package com.carson.gdufs_sign_system.utils

import android.util.Log
import com.tencent.mapsdk.raster.model.LatLng
import java.util.*

object Const {
    private const val TAG = "Const"

    const val BAIDU_APP_ID = "18644584"
    const val BAIDU_API_KEY = "sKp19MW2258wollwUi0RQG2j"
    const val BAIDU_SECRET_KEY = "Y46UgQ9GkgK9RTeD6vYQMGfVYTvOwV04"

    val GDUFS_LATLNG = LatLng(23.06554767726593, 113.39734911918642)
    val GDUFS_STR = "广东外语外贸大学"

    fun getCurrentTime(): String {
        val mCalender = Calendar.getInstance()
        val mCurrentTime = mCalender.get(Calendar.HOUR_OF_DAY).toString() + ":" +
                mCalender.get(Calendar.MINUTE).toString() + ":" + mCalender.get(Calendar.SECOND)
        Log.i(TAG, mCurrentTime)
        return mCurrentTime
    }
}