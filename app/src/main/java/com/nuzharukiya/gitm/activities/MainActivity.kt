package com.nuzharukiya.gitm.activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.nuzharukiya.gitm.R
import com.nuzharukiya.gitm.models.PullRequestModel
import com.nuzharukiya.gitm.presenters.MainActivityPresenter
import com.nuzharukiya.gitm.utils.ActivityBase
import com.nuzharukiya.gitm.utils.BaseUtils
import com.nuzharukiya.gitm.views.MainActivityView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        ActivityBase,
        MainActivityView {

    private lateinit var mContext: Context
    private lateinit var baseUtils: BaseUtils
    private lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initApp()
    }

    override fun initApp() {
        mContext = this
        baseUtils = BaseUtils(mContext)
        presenter = MainActivityPresenter(this)
    }

    override fun initViews() {
        initLinkListener()
    }

    private fun initLinkListener() {
        ibLookup.setOnClickListener {
            if (checkNetwork() && verifyUserRepo()) {
                fetchPRs(getUserRepo())
            }
        }
    }

    override fun initData() {
        showNoData()
    }

    override fun verifyUserRepo(): Boolean {
        return getUserRepo().isNotEmpty() // TODO
    }

    override fun fetchPRs(userRepo: String) {//TODO
    }

    override fun displayPR(prModel: PullRequestModel) {//TODO
    }

    override fun showLoader() {
        pbLoader.visibility = View.VISIBLE
    }

    override fun dismissLoader() {
        pbLoader.visibility = View.GONE
    }

    override fun checkNetwork(): Boolean {
        if (baseUtils.isOnline()) {
            return true
        } else {
            showSnackbar(resMessage = R.string.no_internet)
        }
        return false
    }

    override fun showSnackbar(resMessage: Int) {
        Snackbar.make(clParent, resources.getString(resMessage), Snackbar.LENGTH_LONG).show()
    }

    override fun showNoData(resMessage: Int, bNoData: Boolean) {
        tvNoData.visibility = if (bNoData) View.GONE else View.VISIBLE

        if (bNoData && !isLoaderVisible()) {
            tvNoData.text = resources.getString(if (getUserRepo().isEmpty()) R.string.input_required else R.string.no_data)
        }
    }

    private fun isLoaderVisible(): Boolean {
        return pbLoader.visibility == View.VISIBLE
    }

    private fun getUserRepo(): String {
        return etId.text.toString().trim()
    }
}
