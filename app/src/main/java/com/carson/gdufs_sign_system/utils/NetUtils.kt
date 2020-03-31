package com.carson.gdufs_sign_system.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.CoroutineContext

object NetUtils {
    fun <T> getRetrofitClient(url: String, clazz: Class<T>, needGsonConverter: Boolean): T {
        val builder = Retrofit.Builder()
        builder.baseUrl(url)
        if (needGsonConverter) builder.addConverterFactory(GsonConverterFactory.create())
        val client = getOkHttpClient().newBuilder()
            .addInterceptor(getLoggingInterceptor())
            .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
            .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
        builder.client(client.build())
        return builder.build().create(clazz)
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor.Logger {
            try {
                val text = URLDecoder.decode(it, "UTF-8")
                Log.e("OKHttp-----", text)
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                Log.e("OKHttp-----", it)
            }
        }
        return HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm())
        val keyStore: KeyStore? = null
        trustManagerFactory.init(keyStore)
        val trustManagers = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) { ("Unexpected default trust managers:"
            + Arrays.toString(trustManagers)) }
        val trustManager = trustManagers[0] as X509TrustManager


        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        val sslSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder().apply {
            sslSocketFactory(sslSocketFactory, trustManager)
        }
        return builder.build()
    }
}