package com.dicoding.prayogo.moviecatalogue.api

import com.dicoding.prayogo.moviecatalogue.BuildConfig
import com.dicoding.prayogo.moviecatalogue.model.Genres
import com.dicoding.prayogo.moviecatalogue.model.MovieDb
import com.dicoding.prayogo.moviecatalogue.MainActivity
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Observable

interface ApiMovieDb {
    @GET("movie/now_playing")
    fun getNowPlayingMovie(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = MainActivity.LANGUAGE_FILM.toString(),
        @Query("page") page: Int
    ): Observable<MovieDb>

    @GET("tv/on_the_air")
    fun getOnTheAirTVShow(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String =  MainActivity.LANGUAGE_FILM.toString(),
        @Query("page") page: Int
    ): Observable<MovieDb>

    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = MainActivity.LANGUAGE_FILM.toString()
    ): Observable<Genres>
}