package com.nuzharukiya.gitm.utils

import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient



/**
 * Created by Nuzha Rukiya on 19/07/19.
 */

class GMApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidNetworking.initialize(applicationContext, getOkHttpClient())
    }

    private fun getOkHttpClient(): OkHttpClient? {
        return OkHttpClient().newBuilder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()
    }
}