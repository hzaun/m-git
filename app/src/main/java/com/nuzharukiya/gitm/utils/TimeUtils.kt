package com.nuzharukiya.gitm.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Nuzha Rukiya on 19/07/20.
 */

class TimeUtils {

    companion object {
        private val timeUtils = TimeUtils()

        fun getInstance(): TimeUtils {
            return timeUtils
        }
    }

    private val SDF_DATETIME = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault())
}