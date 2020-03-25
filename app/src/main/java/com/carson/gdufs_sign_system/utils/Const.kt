package com.carson.gdufs_sign_system.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.tencent.mapsdk.raster.model.LatLng
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

object Const {
    private const val TAG = "Const"

    private var mPrefKey = "GDUFS_SIGN_SYSTEM"

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

    const val REQUEST_CODE_FROM_DETAIL_TO_SIGN = 0x3002       // 从detailfragment去signfragment签到

    const val RESULT_CODE_SIGN_SUCCESS = 0x4002

    const val REQUEST_CODE_FROM_HOME_TO_DETAIL = 0x3003       // 从homefragment去detailfragment

    const val BASE_URL = "http://111.230.181.17:8888/myWeb/"

    object Net {
        const val RESPONSE_SUCCESS = "200"
        const val RESPONSE_CLIENT_ERROR = "400"
        const val RESPONSE_SERVER_ERROR = "500"

        const val ERR_MSG_COMMON = "请求数据有误"
    }

    object SearchApi {
        const val SEARCH_MAP_API = "https://apis.map.qq.com/tools/locpicker?search=1&type=0&" +
                "&backurl=http://callback&zoom=15&policy" +
                "=1&mapdraggable=1&key=6DWBZ-AWUKG-WX4QP-IGCIO-O6K76-2XFME&referer=sign_system"
    }

    private const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd"

    object PreferenceKeys {
        const val USER_ID = "userId"
        const val AUTH_IMAGE = "authImage"
        const val IDENTITY = "identity"
    }

    object BundleKeys {
        const val DETAIL_ID = "detail_id"
        const val SIGN_LAT = "sign_latitude"
        const val SIGN_LNG = "sign_longitude"
        const val SIGN_RADIUS = "sign_radius"
        const val SIGN_PLACE = "sign_place"
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.US)
        return sdf.format(Date())
    }

    fun getSpecDate(date: Date): String {
        val sdf = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.US)
        return sdf.format(date)
    }

    fun getCurrentTime(): String {
        val mCalender = Calendar.getInstance()
        val hour = mCalender.get(Calendar.HOUR_OF_DAY)
        val minute = mCalender.get(Calendar.MINUTE)
        val seconds = mCalender.get(Calendar.SECOND)
        val hourStr = if (hour < 10) "0$hour" else hour.toString()
        val minuteStr = if (minute < 10) "0$minute" else minute.toString()
        val secondStr = if (seconds < 10) "0$seconds" else seconds.toString()

        val mCurrentTime = hourStr + ":" + minuteStr + ":" + secondStr
        Log.i(TAG, mCurrentTime)
        return mCurrentTime
    }

    fun getSharedPreference(contextRef: WeakReference<Context?>): SharedPreferences? {
        return contextRef.get()?.getSharedPreferences(mPrefKey, Context.MODE_PRIVATE)
    }
}