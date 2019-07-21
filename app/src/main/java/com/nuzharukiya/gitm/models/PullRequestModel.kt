package com.nuzharukiya.gitm.models

/**
 * Created by Nuzha Rukiya on 19/07/19.
 */

data class PullRequestModel(
        val title: String = "",
        val number: Int = -1,
        val user: UserModel = UserModel(),
        val created_at: String = "",
        val bLoader: Boolean = false
)

data class UserModel(
        val login: String = ""
)
