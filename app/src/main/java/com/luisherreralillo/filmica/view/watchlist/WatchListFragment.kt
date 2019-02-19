package com.luisherreralillo.filmica.view.watchlist


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luisherreralillo.filmica.R
import kotlinx.android.synthetic.main.fragment_watch_list.*

class WatchListFragment : Fragment() {

    val adapter: WatchlistAdapter by lazy {
        val instance = WatchlistAdapter()
        instance
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_watch_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        watchlist.adapter = adapter
    }

}
