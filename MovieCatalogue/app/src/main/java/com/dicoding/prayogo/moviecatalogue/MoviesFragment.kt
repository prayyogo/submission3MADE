package com.dicoding.prayogo.moviecatalogue


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.dicoding.prayogo.moviecatalogue.adapter.AdapterMovieDb
import com.dicoding.prayogo.moviecatalogue.api.ApiMovieDb
import com.dicoding.prayogo.moviecatalogue.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_movies.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log
import kotlin.properties.Delegates


class MoviesFragment : Fragment() {

    private var adapterMovieDb by Delegates.notNull<AdapterMovieDb>()
    private var isLoading by Delegates.notNull<Boolean>()
    private var page by Delegates.notNull<Int>()
    private var totalPage by Delegates.notNull<Int>()
    private var listGenreMovieDb by Delegates.notNull<ArrayList<Genre>>()
    private var resultTheMovieDb by Delegates.notNull<ArrayList<Result>>()
    private var movieDb by Delegates.notNull<MovieDb>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        page = 1
        totalPage = 0
        if(savedInstanceState!=null){
            page=savedInstanceState.getInt("Page")
            totalPage=savedInstanceState.getInt("TotalPage")
            movieDb=savedInstanceState.getParcelable("DbMovie")
            resultTheMovieDb=savedInstanceState.getParcelableArrayList("Result")
            listGenreMovieDb=savedInstanceState.getParcelableArrayList("Genre")
            hideLoading()
            adapterMovieDb = AdapterMovieDb(
                this.activity!!,
                resultTheMovieDb,
                listGenreMovieDb
            )
            if(page==1){
                rv_movie.layoutManager = LinearLayoutManager(context)
                rv_movie.adapter = adapterMovieDb
            }else{
                rv_movie.layoutManager = LinearLayoutManager(context)
                rv_movie.adapter = adapterMovieDb
                adapterMovieDb.refreshAdapter(resultTheMovieDb)
            }
        }else{
            doLoadData()
        }
        initListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    @Override
    override fun onSaveInstanceState(outState:Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("Result",resultTheMovieDb)
        outState.putParcelableArrayList("Genre",listGenreMovieDb)
        outState.putParcelable("DbMovie",movieDb)
        outState.putInt("TotalPage",totalPage)
        outState.putInt("Page",page)
    }
    @SuppressLint("CheckResult")
    private fun doLoadData() {
        showLoading(true)
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        val apiMovieDb = retrofit.create(ApiMovieDb::class.java)
        apiMovieDb.getGenres()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { genreMovieDB: Genres ->
                    listGenreMovieDb = genreMovieDB.genres as ArrayList
                },
                { t: Throwable ->
                    t.printStackTrace()
                },
                {
                    hideLoading()
                }
            )
        apiMovieDb.getNowPlayingMovie(page = page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { theMovieDb: MovieDb ->
                    resultTheMovieDb = theMovieDb.results as ArrayList
                    movieDb=theMovieDb
                    if (page == 1) {
                        adapterMovieDb = AdapterMovieDb(
                            this.activity!!,
                            resultTheMovieDb,
                            listGenreMovieDb
                        )
                        rv_movie.layoutManager = LinearLayoutManager(activity)
                        rv_movie.adapter = adapterMovieDb
                    } else {
                        adapterMovieDb.refreshAdapter(resultTheMovieDb)
                    }
                    totalPage = movieDb.totalPages
                },
                { t: Throwable ->
                    t.printStackTrace()
                },
                {
                    hideLoading()
                }
            )
    }
    private fun initListener() {
        rv_movie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                Log.d("item",countItem.toString())
                val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
                Log.d("posisi",lastVisiblePosition.toString())
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && page < totalPage) {
                    showLoading(true)
                    page = page.plus(1)
                    doLoadData()
                }
            }
        })
        // Click
        rv_movie.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            var gestureDetector = GestureDetector(
                activity?.applicationContext,
                object : GestureDetector.SimpleOnGestureListener() {

                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
               // val linearLayoutManager = rv.layoutManager as LinearLayoutManager
                val movieItem = adapterMovieDb.getItem()
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    val position = rv.getChildAdapterPosition(child)
                    val movieGenre=adapterMovieDb.getGenres(movieItem[position].genreIds)
                    showDetailFilm(movieItem[position],movieGenre)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }
        })
    }

    private fun showDetailFilm(film: Result, genreFilm: String) {
        val dataFilm= Film(film.posterPath,film.title,film.overview,film.releaseDate,film.voteAverage,genreFilm,film.popularity)
        val moveObjectIntent= Intent(activity,DetailFilmActivity::class.java)
        moveObjectIntent.putExtra(DetailFilmActivity.EXTRA_FILM,dataFilm)
        startActivity(moveObjectIntent)
    }

    private fun showLoading(isRefresh: Boolean) {
        isLoading = true
        progress_bar_movie.visibility = View.VISIBLE
        rv_movie.visibility.let {
            if (isRefresh) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun hideLoading() {
        isLoading = false
        progress_bar_movie.visibility = View.GONE
        rv_movie.visibility = View.VISIBLE
    }
}
