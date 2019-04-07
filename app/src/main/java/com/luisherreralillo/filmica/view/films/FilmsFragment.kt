package com.luisherreralillo.filmica.view.films

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.FilmsRepo
import com.luisherreralillo.filmica.view.util.EndlessRecyclerViewScrollListener
import com.luisherreralillo.filmica.view.util.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*


class FilmsFragment : Fragment(), FilmReloadItems {

    lateinit var listener: FilmItemClickListener

    val list: RecyclerView by lazy {
        val instance = view!!.findViewById<RecyclerView>(R.id.listFilms)
        //instance.layoutManager = LinearLayoutManager(this.context)
        instance.addItemDecoration(ItemOffsetDecoration(R.dimen.offset_grid))
        instance.setHasFixedSize(true)

        instance
    }

    val adapter: FilmsAdapter by lazy {
        val instance = FilmsAdapter { film ->
            this.listener.onItemClicked(film)
        }

        instance
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is FilmItemClickListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_films, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scrollListener =
            EndlessRecyclerViewScrollListener(list.layoutManager as GridLayoutManager) { page, totalItemsCount, view ->
                view.post {
                    reloadNewFilms(page, totalItemsCount)
                }
            }

        list.addOnScrollListener(scrollListener)
        list.adapter = adapter

        btnRetry?.setOnClickListener { this.reloadInitialFilms() }
    }

    override fun onResume() {
        super.onResume()
        this.reloadInitialFilms()

    }

    override fun reloadInitialFilms(page: Int) {
        FilmsRepo.discoverFilms(context!!, page, { films ->
            progress?.visibility = View.INVISIBLE
            layoutError?.visibility = View.INVISIBLE
            list.visibility = View.VISIBLE
            adapter.setFilms(films)
        }, { error ->
            list.visibility = View.INVISIBLE
            progress?.visibility = View.INVISIBLE
            layoutError?.visibility = View.VISIBLE

            error.printStackTrace()
        })
    }

    override fun reloadNewFilms(page: Int, positionStart: Int) {
        FilmsRepo.discoverFilms(context!!, page, { films ->
            adapter.addFilms(films, positionStart, films.size)
        }, { error ->
            error.printStackTrace()
        })
    }
}
