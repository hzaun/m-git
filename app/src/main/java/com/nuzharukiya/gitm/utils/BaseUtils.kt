package com.nuzharukiya.gitm.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
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

    /**
     * Force hide keyboard
     */
    fun forceHideKeyboard() {
        if (isKeyboardVisible()) {
            val imm = mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }

    private fun isKeyboardVisible(): Boolean {
        ///This method is based on the one described at http://stackoverflow.com/questions/4745988/how-do-i-detect-if-software-keyboard-is-visible-on-android-device
        val r = Rect()
        val contentView = (mContext as Activity).findViewById<View>(android.R.id.content)
        contentView.getWindowVisibleDisplayFrame(r)
        val screenHeight = contentView.rootView.height

        val keypadHeight = screenHeight - r.bottom

        return keypadHeight > screenHeight * 0.15
    }
}