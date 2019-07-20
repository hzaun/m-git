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

    private val locale = Locale.getDefault()
    private val SDF_DATETIME = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", locale)
    private val SDF_SIMPLE = SimpleDateFormat("MMM dd", locale)

    fun convertDateTimeToSimpleFormat(dt: String): String {
        if (dt.isStringDateInvalid()) return ""

        val date = SDF_DATETIME.parse(dt)
        val calDt = Calendar.getInstance()
        calDt.time = date
        val dtYear = calDt.get(Calendar.YEAR)
        val curYear = Calendar.getInstance().get(Calendar.YEAR)

        return """${SDF_SIMPLE.format(date)}${if (dtYear == curYear) "" else ", $curYear"}"""
    }
}