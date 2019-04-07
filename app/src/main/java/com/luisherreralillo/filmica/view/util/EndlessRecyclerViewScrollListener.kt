package com.luisherreralillo.filmica.view.util

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager


class EndlessRecyclerViewScrollListener : RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var visibleThreshold = 5

    // The current offset index of data you have loaded
    private var currentPage = 1

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private val startingPageIndex = 1

    private var mLayoutManager: RecyclerView.LayoutManager? = null

    // Defines the process for actually loading more data based on page
    var onLoadMoreCallback: ((page: Int, totalItemsCount: Int, view: RecyclerView) -> Unit)? = null

    constructor(layoutManager: LinearLayoutManager, onLoadMoreCallback: ((page: Int, totalItemsCount: Int, view: RecyclerView) -> Unit)? = null) {
        this.mLayoutManager = layoutManager
        this.onLoadMoreCallback = onLoadMoreCallback
    }

    constructor(layoutManager: GridLayoutManager, onLoadMoreCallback: ((page: Int, totalItemsCount: Int, view: RecyclerView) -> Unit)? = null) {
        this.mLayoutManager = layoutManager
        visibleThreshold = visibleThreshold * layoutManager.spanCount
        this.onLoadMoreCallback = onLoadMoreCallback

    }

    constructor(layoutManager: StaggeredGridLayoutManager, onLoadMoreCallback: ((page: Int, totalItemsCount: Int, view: RecyclerView) -> Unit)? = null) {
        this.mLayoutManager = layoutManager
        visibleThreshold = visibleThreshold * layoutManager.spanCount
        this.onLoadMoreCallback = onLoadMoreCallback
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager!!.itemCount

        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions =
                (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount
            && view.adapter!!.itemCount > visibleThreshold
        ) {// This condition will useful when recyclerview has less than visibleThreshold items
            currentPage++
            onLoadMoreCallback?.invoke(currentPage, totalItemCount, view)
            loading = true
        }
    }

    // Call whenever performing new searches
    fun resetState() {
        this.currentPage = this.startingPageIndex
        this.previousTotalItemCount = 0
        this.loading = true
    }

}