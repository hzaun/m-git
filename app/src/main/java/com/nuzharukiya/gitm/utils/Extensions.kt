package com.nuzharukiya.gitm.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Nuzha Rukiya on 19/07/20.
 */

// Kotlin Extensions

/**
 *  Parent viewGroup can be inflated with only the
 *  @param layout id
 */
fun ViewGroup.inflate(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}
