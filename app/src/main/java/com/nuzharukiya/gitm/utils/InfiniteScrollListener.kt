package com.nuzharukiya.gitm.utils

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager


/**
 * Created by Nuzha Rukiya on 19/07/21.
 */
abstract class InfiniteScrollListener(private var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }

        onSetScrolled(layoutManager.findFirstCompletelyVisibleItemPosition() < visibleItemCount)
    }

    protected abstract fun loadMoreItems()
    protected abstract fun onSetScrolled(bFirstSetVisible: Boolean)
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}