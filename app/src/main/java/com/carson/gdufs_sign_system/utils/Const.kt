package com.carson.gdufs_sign_system.utils

import android.util.Log
import com.tencent.mapsdk.raster.model.LatLng
import java.util.*

object Const {
    private const val TAG = "Const"

    const val BAIDU_APP_ID = "18644686"
    const val BAIDU_API_KEY = "onWXf91wuRB0dlzpCrjQmrPr"
    const val BAIDU_SECRET_KEY = "OAa6QLg4rL37duCgVX1bLaY5E3kgNmZo"

    val GDUFS_LATLNG = LatLng(23.06554767726593, 113.39734911918642)
    val GDUFS_STR = "广东外语外贸大学"

    const val SCAN_ENTER_FLAG = "scan_enter_flag"
    const val SCAN_ENTER_SUBMIT = 0x2001  // 进行提交人像
    const val SCAN_ENTER_COMPARE = 0X2002 // 进行比对人像

    const val REQUEST_CODE_FROM_MAP_TO_SCAN_COMPARE = 0x3001   // 从mapfragment去scanfragment比对人脸

    const val RESULT_CODE_COMPARE_SUCCESS = 0x4001   // 人脸比对成功的结果码

    const val BASE_URL = "http://111.230.181.17:8888"

    fun getCurrentTime(): String {
        val mCalender = Calendar.getInstance()
        val mCurrentTime = mCalender.get(Calendar.HOUR_OF_DAY).toString() + ":" +
                mCalender.get(Calendar.MINUTE).toString() + ":" + mCalender.get(Calendar.SECOND)
        Log.i(TAG, mCurrentTime)
        return mCurrentTime
    }
}