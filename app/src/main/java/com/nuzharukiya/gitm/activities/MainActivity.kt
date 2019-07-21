package com.nuzharukiya.gitm.activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.nuzharukiya.gitm.R
import com.nuzharukiya.gitm.adapters.PullReqAdapter
import com.nuzharukiya.gitm.models.PullRequestModel
import com.nuzharukiya.gitm.presenters.MainActivityPresenter
import com.nuzharukiya.gitm.utils.ActivityBase
import com.nuzharukiya.gitm.utils.BaseUtils
import com.nuzharukiya.gitm.utils.ValidationUtils
import com.nuzharukiya.gitm.views.MainActivityView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import android.text.Editable
import android.text.TextWatcher
import com.nuzharukiya.gitm.utils.InfiniteScrollListener

class MainActivity : AppCompatActivity(),
        ActivityBase,
        MainActivityView {

    private lateinit var mContext: Context
    private lateinit var baseUtils: BaseUtils
    private lateinit var presenter: MainActivityPresenter

    // Pull Request List
    private var prAdapter: PullReqAdapter? = null
    private var lsUser: String = ""
    private var lsRepo: String = ""

    // Infinite Scroll/ Pagination
    private var CUR_PAGE = 1
    private var bIsLoading = false
    private var bIsLastPage = false

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
        checkForNext()
        checkForSearch()
        initLinkListener()

        setPRAdapter()
        scrollToTop()
    }

    private fun scrollToTop() {
        fabScrollToTop.setOnClickListener {
            rvPullRequests.smoothScrollToPosition(0)
        }
        fabScrollToTop.hide()
    }

    private fun checkForNext() {
        etUser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                // Change focus on "/"
                val str = s.toString()
                if (str.contains("/")) {
                    etUser.setText(str.replace("/", ""))
                    etRepo.isFocusableInTouchMode = true
                    etRepo.requestFocus()
                }
            }
        })
    }

    private fun checkForSearch() {
        etRepo.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getPRs()
                    return true
                }
                return false
            }
        })
    }

    private fun initLinkListener() {
        acivLookup.setOnClickListener {
            getPRs()
        }
    }

    private fun getPRs() {
        if (checkNetwork()) {
            if (verifyUserRepo(bShowMessage = true)) {
                lsUser = getUser()
                lsRepo = getRepo()
                resetConstraints()

                fetchPRs(lsUser, lsRepo)
            } else {
                dismissLoader()
            }
        }
    }

    private fun resetConstraints() {
        CUR_PAGE = 1
        bIsLoading = false
        bIsLastPage = false
    }

    override fun initData() {
        showNoData()
    }

    override fun verifyUserRepo(bShowMessage: Boolean): Boolean {
        val user = etUser.text.toString().trim()
        val repo = etRepo.text.toString().trim()

        if (user.isNotEmpty() && repo.isNotEmpty()) {
            if (user.length >= 40 || !ValidationUtils.getInstance().validateUsername(user)) {
                showSnackbar(R.string.invalid_username)
                return false
            }
            return true
        }

        if (bShowMessage) {
            if (user.isEmpty()) {
                showSnackbar(R.string.owner_required)
            } else if (repo.isEmpty()) {
                showSnackbar(R.string.repo_required)
            }
        }

        return false
    }

    override fun fetchPRs(user: String, repo: String) {
        presenter.getPRObservable(user, repo, CUR_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(presenter.getPRObserver())
    }

    override fun displayPR(prList: List<PullRequestModel>) {
        if (CUR_PAGE == 1) {
            prAdapter?.clear()
            if(prList.isEmpty()) prAdapter?.notifyDataSetChanged()
        } else {
            prAdapter?.removeLoadingFooter()
        }

        if (prList.isNotEmpty()) prAdapter?.addAll(prList)

        if (prList.isEmpty() || prList.size % 30 != 0) {
            bIsLastPage = true
        }
        bIsLoading = false

        showNoData(bNoData = prAdapter?.isEmpty() == true)
    }

    private fun setPRAdapter() {
        if (prAdapter == null) {
            prAdapter = PullReqAdapter()

            rvPullRequests.itemAnimator = DefaultItemAnimator()
            val layoutManager = LinearLayoutManager(mContext)
            rvPullRequests.layoutManager = layoutManager
            rvPullRequests.adapter = prAdapter

            initOnScrollListener(layoutManager)
        }
    }

    private fun initOnScrollListener(layoutManager: LinearLayoutManager) {
        rvPullRequests.addOnScrollListener(object : InfiniteScrollListener(layoutManager) {
            override fun loadMoreItems() {
                bIsLoading = true
                //Increment page index to load the next one
                CUR_PAGE += 1
                loadNextPage()
            }

            override fun onSetScrolled(bFirstSetVisible: Boolean) {
                reflectOnScrollToTop(bFirstSetVisible)
            }

            override fun isLastPage(): Boolean {
                return bIsLastPage
            }

            override fun isLoading(): Boolean {
                return bIsLoading
            }
        })
    }

    private fun reflectOnScrollToTop(bHide: Boolean = false) {
        if (bHide)
            fabScrollToTop.hide()
        else
            fabScrollToTop.show()
    }

    private fun loadNextPage() {
        if (!bIsLastPage) prAdapter?.addLoadingFooter()
        fetchPRs(lsUser, lsRepo)
    }

    override fun showLoader() {
        pbLoader.visibility = if (bIsLoading) View.GONE else View.VISIBLE
    }

    override fun dismissLoader() {
        pbLoader.visibility = View.GONE
    }

    override fun checkNetwork(): Boolean {
        if (baseUtils.isOnline()) {
            showLoader()
            baseUtils.forceHideKeyboard()

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
        tvNoData.visibility = if (bNoData) View.VISIBLE else View.GONE

        if (bNoData && !isLoaderVisible()) {
            if (resMessage > 0) {
                tvNoData.text = resources.getString(resMessage)
            } else {
                tvNoData.text = resources.getString(if (!verifyUserRepo()) R.string.input_required else R.string.no_data)
            }
        }
    }

    private fun isLoaderVisible(): Boolean {
        return pbLoader.visibility == View.VISIBLE
    }

    private fun getUser(): String {
        return etUser.text.toString().trim()
    }

    private fun getRepo(): String {
        return etRepo.text.toString().trim()
    }
}
