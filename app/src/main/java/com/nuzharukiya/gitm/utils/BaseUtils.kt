package com.nuzharukiya.gitm.utils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.ProgressBar


/**
 * Created by Nuzha Rukiya on 19/07/19.
 */

class BaseUtils(private val mContext: Context) {

    private var progressBar: ProgressBar? = null

    fun isOnline(): Boolean {
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        var isConnected = false
        if (connectivityManager != null) {
            val activeNetwork = connectivityManager.activeNetworkInfo
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

        return isConnected
    }
}