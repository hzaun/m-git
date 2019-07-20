package com.nuzharukiya.gitm.activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nuzharukiya.gitm.R
import com.nuzharukiya.gitm.adapters.PullReqAdapter
import com.nuzharukiya.gitm.models.PullRequestModel
import com.nuzharukiya.gitm.presenters.MainActivityPresenter
import com.nuzharukiya.gitm.utils.ActivityBase
import com.nuzharukiya.gitm.utils.BaseUtils
import com.nuzharukiya.gitm.views.MainActivityView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
        ActivityBase,
        MainActivityView {

    private lateinit var mContext: Context
    private lateinit var baseUtils: BaseUtils
    private lateinit var presenter: MainActivityPresenter

    private var prAdapter: PullReqAdapter? = null
    private val pullRequests = ArrayList<PullRequestModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initApp()
    }

    override fun initApp() {
        mContext = this
        baseUtils = BaseUtils(mContext)
        presenter = MainActivityPresenter(this)

        initViews()
        initData()
    }

    override fun initViews() {
        initLinkListener()
    }

    private fun initLinkListener() {
        acibLookup.setOnClickListener {
            if (checkNetwork()) {
                if (verifyUserRepo()) {
                    fetchPRs(getUserRepo())
                } else {
                    showSnackbar(R.string.input_required)
                }
            }
        }
    }

    override fun initData() {
        showNoData()
    }

    override fun verifyUserRepo(): Boolean {
        return getUserRepo().isNotEmpty() // TODO
    }

    override fun fetchPRs(userRepo: String) {
        presenter.getPRObservable("hzaun", "windows-wifi-hotspot")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(presenter.getPRObserver())
    }

    override fun displayPR(prModel: PullRequestModel) {//TODO
    }

    fun initAdapter() {
        if (prAdapter == null) {
            prAdapter = PullReqAdapter(pullRequests)

            rvPullRequests.itemAnimator = DefaultItemAnimator()
            rvPullRequests.layoutManager = LinearLayoutManager(mContext)
            rvPullRequests.adapter = prAdapter
        } else prAdapter?.notifyDataSetChanged()

        showNoData(bNoData = pullRequests.isEmpty())
    }

    override fun showLoader() {
        pbLoader.visibility = View.VISIBLE
    }

    override fun dismissLoader() {
        pbLoader.visibility = View.GONE
    }

    override fun checkNetwork(): Boolean {
        if (baseUtils.isOnline()) {
            showLoader()

            return true
        }

        showSnackbar(resMessage = R.string.no_internet)
        return false
    }

    override fun showSnackbar(resMessage: Int, sMessage: String) {
        if (sMessage.isNotEmpty())
            Snackbar.make(clParent, sMessage, Snackbar.LENGTH_LONG).show()
        else if (resMessage > 0)
            Snackbar.make(clParent, resources.getString(resMessage), Snackbar.LENGTH_LONG).show()
    }

    override fun showNoData(resMessage: Int, bNoData: Boolean) {
        tvNoData.visibility = if (bNoData) View.GONE else View.VISIBLE

        if (bNoData && !isLoaderVisible()) {
            if (resMessage > 0) {
                tvNoData.text = resources.getString(resMessage)
            } else {
                tvNoData.text = resources.getString(if (getUserRepo().isEmpty()) R.string.input_required else R.string.no_data)
            }
        }
    }

    private fun isLoaderVisible(): Boolean {
        return pbLoader.visibility == View.VISIBLE
    }

    private fun getUserRepo(): String {
        return etId.text.toString().trim()
    }
}
