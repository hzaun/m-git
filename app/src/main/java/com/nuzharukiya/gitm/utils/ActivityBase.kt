package com.nuzharukiya.gitm.utils

/**
 * Created by Nuzha Rukiya on 19/07/19.
 */

interface ActivityBase {

    /**
     * Set the mContext and init UIComponents
     */
    fun initApp()

    /**
     * Find views and set their values
     */
    fun initViews()

    /**
     * Initialize the data
     */
    fun initData()

    /**
     * Start doing the work
     */
    fun runFactory()
}