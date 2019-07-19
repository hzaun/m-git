package com.nuzharukiya.gitm.views

import com.nuzharukiya.gitm.models.PullRequestModel

/**
 * Created by Nuzha Rukiya on 19/07/19.
 */
interface MainActivityView {

    fun verifyUserRepo(): Boolean

    fun fetchPRs(userRepo: String)

    fun displayPR(prModel: PullRequestModel)

    fun stopLoader()

    fun checkNetwork()
}