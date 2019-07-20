package com.nuzharukiya.gitm.views

import com.nuzharukiya.gitm.models.PullRequestModel

/**
 * Created by Nuzha Rukiya on 19/07/19.
 */
interface MainActivityView {

    fun verifyUserRepo(bShowMessage: Boolean = false): Boolean

    fun fetchPRs(user: String, repo: String)

    fun displayPR(prList: List<PullRequestModel>)

    fun showLoader()

    fun dismissLoader()

    fun checkNetwork(): Boolean

    fun showSnackbar(resMessage: Int = -1, sMessage: String = "")

    fun showNoData(resMessage: Int = -1, bNoData: Boolean = true)
}