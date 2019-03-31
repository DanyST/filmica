package com.luisherreralillo.filmica.view.trends

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.FilmsRepo
import com.luisherreralillo.filmica.view.films.FilmItemClickListener
import com.luisherreralillo.filmica.view.films.FilmsAdapter
import com.luisherreralillo.filmica.view.util.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_films.*
import kotlinx.android.synthetic.main.layout_error.*

class TrendsFragment: Fragment() {

    // Utilizar el mismo adaptador y layout resource de fragment films, SOLO CAMBIAR el contenido de reload()

    lateinit var listener: FilmItemClickListener

    val list: RecyclerView by lazy {
        listFilms.addItemDecoration(ItemOffsetDecoration(R.dimen.offset_grid))
        listFilms.setHasFixedSize(true)

        listFilms
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
        list.adapter = adapter

        btnRetry?.setOnClickListener { this.reload() }
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    fun reload() {
        FilmsRepo.trendingFilms(context!!, { films ->
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
}