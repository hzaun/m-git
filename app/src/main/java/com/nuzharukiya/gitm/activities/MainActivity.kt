package com.nuzharukiya.gitm.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.nuzharukiya.gitm.R

import butterknife.OnClick
import com.nuzharukiya.gitm.models.PullRequestModel
import com.nuzharukiya.gitm.presenters.MainActivityPresenter
import com.nuzharukiya.gitm.utils.ActivityBase
import com.nuzharukiya.gitm.views.MainActivityView

class MainActivity : AppCompatActivity(),
        ActivityBase,
        MainActivityView {

    private lateinit var mContext: Context

    private lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initApp() {//TODO
    }

    override fun initViews() {//TODO
    }

    override fun initData() {//TODO
    }

    override fun runFactory() {//TODO
    }

    override fun verifyUserRepo(): Boolean {//TODO
    }

    override fun fetchPRs(userRepo: String) {//TODO
    }

    override fun displayPR(prModel: PullRequestModel) {//TODO
    }

    override fun stopLoader() {//TODO
    }

    override fun checkNetwork() {//TODO
    }
}
