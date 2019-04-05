package com.luisherreralillo.filmica.view.watchlist


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.FilmsRepo
import com.luisherreralillo.filmica.view.films.FilmItemClickListener
import com.luisherreralillo.filmica.view.util.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_watch_list.*

class WatchListFragment : Fragment() {

    lateinit var listener: FilmItemClickListener

    val adapter: WatchlistAdapter by lazy {
        val instance = WatchlistAdapter { film ->
            this.listener?.onItemClicked(film)
        }
        instance
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FilmItemClickListener) {
            listener = context
        }
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

        setupSwipeHandler()
        watchlist.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        FilmsRepo.watchlist(context!!) { films ->
            adapter.setFilms(films.toMutableList())
        }
    }

    private fun setupSwipeHandler() {
        val swipeHandler = object: SwipeToDeleteCallback() {
            override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int) {
                deleteFilmAt(holder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(watchlist)
    }

    private fun deleteFilmAt(position: Int) {
        val film = adapter.getFilm(position)
        FilmsRepo.deleteFilm(context!!, film) {
            adapter.removeFilmAt(position)
        }
    }

}
