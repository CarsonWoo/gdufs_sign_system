package com.carson.gdufs_sign_system.manager.post.controller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.*
import android.widget.Toast
import com.carson.gdufs_sign_system.manager.post.SearchLocationActivity
import com.carson.gdufs_sign_system.utils.Const
import com.tencent.mapsdk.raster.model.LatLng
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.net.URLDecoder

class SearchLocationController(private val contextRef: WeakReference<Context>) {
    private var mWebView: WebView? = null

    private lateinit var mLatLng: LatLng
    private var mAddress: String = ""

    fun setUpWebView(webView: WebView, latLng: LatLng) {
        this.mWebView = webView
        initWebParam(latLng)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebParam(latLng: LatLng) {

        mLatLng = latLng

        val loadUrl = Const.SearchApi.SEARCH_MAP_API.plus("&coord=${latLng.latitude},${latLng.longitude}")

        val webSettings = mWebView?.settings
        webSettings?.apply {
//            setRenderPriority()
            setSupportMultipleWindows(true)
            javaScriptEnabled = true
//            savePassword = true
            setGeolocationEnabled(true)
//            databaseEnabled = true

            javaScriptCanOpenWindowsAutomatically = true
            minimumFontSize += 8
            allowFileAccess = true
            textZoom = 100
        }
        mWebView?.setVerticalScrollbarOverlay(true)

        mWebView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url
//                Log.e("LocationController", "url = ${request?.url?.toString()}")
//                Log.e("LocationController", "url.host = ${url?.toString()}")
//                Toast.makeText(contextRef.get(), "url.host = ${request?.url?.host}", Toast.LENGTH_LONG).show()
                if (url?.toString()?.startsWith("http://callback") == false) {
                    view?.loadUrl(url.toString())
                } else {
                    try {
//                        Log.e("LocationController", "url = ${request?.url?.toString()}")

                        // 转utf-8
                        val decode = URLDecoder.decode(url?.toString(), "UTF-8")
                        Log.e("LocationController", "decode = $decode")

                        // 转Uri
                        val uri = Uri.parse(decode)
                        val latlng = uri.getQueryParameter("latng")
                        val split = latlng?.split(",")
                        val lat = split?.get(0)?.toDouble()
                        val lng = split?.get(1)?.toDouble()
                        val name = uri.getQueryParameter("name")

                        Log.e("LocationController", "address = $name")

                        mLatLng = LatLng(lat?: mLatLng.latitude, lng?: mLatLng.longitude)
                        mAddress = name?: ""
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                }
                return true
            }
        }

        mWebView?.loadUrl(loadUrl)
    }

    fun navigateBackWithData(activity: SearchLocationActivity) {
        Intent().apply {
            putExtra("address", mAddress)
            putExtra("lat", mLatLng.latitude)
            putExtra("lng", mLatLng.longitude)
            activity.let {
                it.setResult(Activity.RESULT_OK, this)
                it.onBackPressed()
            }
        }
    }
}