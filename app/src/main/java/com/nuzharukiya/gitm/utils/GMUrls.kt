package com.nuzharukiya.gitm.utils

import java.lang.StringBuilder
import java.net.URI

/**
 * Created by Nuzha Rukiya on 19/07/19.
 */

interface GMUrls {
    companion object {
        private const val SCHEME = "https"
        private const val BASE_URL = "api.github.com"

        private const val REPOS = "/repos"

        private const val STATE_ALL = "all"
        private const val STATE_OPEN = "open"

        fun buildPullRequestsUrl(owner: String, repos: String): String {
            val uri = URI(
                    SCHEME,
                    BASE_URL,
                    generatePath(owner, repos),
                    "state=$STATE_ALL", // TODO: Change
                    null)

            return uri.toString()
        }

        private fun generatePath(owner: String, repos: String): String {
            return """$REPOS/$owner/$repos/pulls"""
        }
    }
}