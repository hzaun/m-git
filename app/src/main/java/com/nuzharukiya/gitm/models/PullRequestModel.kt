package com.nuzharukiya.gitm.models

/**
 * Created by Nuzha Rukiya on 19/07/19.
 */

data class PullRequestModel(
        val title: String,
        val number: Int,
        val user: UserModel,
        val created_at: String
)

data class UserModel(
        val login: String
)
