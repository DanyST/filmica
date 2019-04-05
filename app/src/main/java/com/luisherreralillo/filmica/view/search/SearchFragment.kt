package com.luisherreralillo.filmica.view.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.luisherreralillo.filmica.R
import com.luisherreralillo.filmica.data.Film
import com.luisherreralillo.filmica.data.FilmsRepo
import com.luisherreralillo.filmica.view.films.FilmItemClickListener
import com.luisherreralillo.filmica.view.util.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_error.*


class SearchFragment : Fragment() {

    lateinit var listener: FilmItemClickListener

    private val list: RecyclerView by lazy {
        listFilms.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                hideKeyboardFrom(context!!, recyclerView.rootView)
            }
        })

        listFilms
    }

    private val adapter: SearchAdapter by lazy {
        val instance = SearchAdapter { film ->
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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.adapter = adapter

        searchFilm.addTextChangedListener(SimpleTextWatcher(
            onTextChangedCallback = { charSequence, start, before, count ->

                if (count > 2 || start > 2) {
                    search(charSequence.toString())
                }
            }
        ))
    }

    private fun search(query: String) {
        progress?.visibility = View.VISIBLE
        list.visibility = View.INVISIBLE
        layoutNotFound.visibility = View.INVISIBLE

        FilmsRepo.searchFilms(context!!, query, { films ->
            progress?.visibility = View.INVISIBLE
            layoutError?.visibility = View.INVISIBLE

            val lastTenFilms = getLastTenFilms(films)

            if (lastTenFilms.isEmpty()) {
                layoutNotFound.visibility = View.VISIBLE
                list.visibility = View.INVISIBLE
            } else {
                list.visibility = View.VISIBLE
            }

            adapter.setFilms(lastTenFilms)

        }, { error ->
            list.visibility = View.INVISIBLE
            progress?.visibility = View.INVISIBLE
            layoutError?.visibility = View.VISIBLE
            error.printStackTrace()
        })
    }

    private fun getLastTenFilms(films: MutableList<Film>) =
        if (films.size > 9) films.subList(0, 10) else films

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
