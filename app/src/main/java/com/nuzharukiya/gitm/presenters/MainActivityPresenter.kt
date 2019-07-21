package com.nuzharukiya.gitm.presenters

import android.util.Log
import com.nuzharukiya.gitm.R
import com.nuzharukiya.gitm.models.PullRequestModel
import com.nuzharukiya.gitm.models.UserModel
import com.nuzharukiya.gitm.utils.GMUrls
import com.nuzharukiya.gitm.views.MainActivityView
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray

/**
 * Created by Nuzha Rukiya on 19/07/19.
 */
class MainActivityPresenter(private val view: MainActivityView) {

    fun getPRObservable(owner: String, repos: String, page: Int): Observable<JSONArray> {
        return Rx2AndroidNetworking.get(GMUrls.buildPullRequestsUrl(owner, repos, page))
                .build()
                .jsonArrayObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getPRObserver(): Observer<JSONArray> {
        return object : Observer<JSONArray> {
            override fun onSubscribe(d: Disposable) {
                view.showLoader()
            }

            override fun onNext(jsonArray: JSONArray) {
                Log.d("T", jsonArray.toString())

                parseResponse(jsonArray)
            }

            override fun onError(e: Throwable) {
                view.showSnackbar(resMessage = R.string.check_owner_repo)
                view.dismissLoader()

                view.displayPR(null)
            }

            override fun onComplete() {
                view.dismissLoader()
            }
        }
    }

    private fun parseResponse(jsonArray: JSONArray) {
        val size = jsonArray.length()
        val prList = ArrayList<PullRequestModel>()

        for (i in 0 until size) {
            val o = jsonArray.optJSONObject(i)

            prList.add(PullRequestModel(
                    o.optString("title"),
                    o.optInt("number"),
                    UserModel(
                            o.optJSONObject("user")
                                    .optString("login")
                    ),
                    o.optString("created_at"),
                    body = o.optString("body")
            ))
        }

        view.displayPR(prList)
    }
}