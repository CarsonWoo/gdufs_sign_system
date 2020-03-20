package com.carson.gdufs_sign_system.manager.post.controller

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.*
import android.widget.Toast
import com.carson.gdufs_sign_system.utils.Const
import java.lang.ref.WeakReference

class SearchLocationController(private val contextRef: WeakReference<Context>) {
    private var mWebView: WebView? = null

    fun setUpWebView(webView: WebView) {
        this.mWebView = webView
        initWebParam()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebParam() {
        val webSettings = mWebView?.settings
        webSettings?.apply {
//            setRenderPriority()
            setSupportMultipleWindows(true)
            javaScriptEnabled = true
//            savePassword = true
            javaScriptCanOpenWindowsAutomatically = true
            minimumFontSize += 8
            allowFileAccess = true
            textZoom = 100
        }
        mWebView?.setVerticalScrollbarOverlay(true)
        mWebView?.webChromeClient = object : WebChromeClient() {

            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false)
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }
        }

        mWebView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Toast.makeText(contextRef.get(), "url.host = ${request?.url?.host}", Toast.LENGTH_LONG).show()
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        mWebView?.loadUrl(Const.SearchApi.SEARCH_MAP_API)
    }
}