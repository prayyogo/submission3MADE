package com.dicoding.prayogo.moviecatalogue.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dicoding.prayogo.moviecatalogue.BuildConfig
import com.dicoding.prayogo.moviecatalogue.R
import com.dicoding.prayogo.moviecatalogue.model.Genre
import com.dicoding.prayogo.moviecatalogue.model.Result
import kotlinx.android.synthetic.main.item_movie.view.*

class AdapterMovieDb (private val context: Context, private var resultMovieDb: ArrayList<Result>,private var genreMovieDb: ArrayList<Genre>) : RecyclerView.Adapter<AdapterMovieDb.ViewHolderTheMovieDb>() {

    private var listFilm= ArrayList<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTheMovieDb =
        ViewHolderTheMovieDb(
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolderTheMovieDb, position: Int) {
        val resultItem = resultMovieDb[position]
        Glide
            .with(context)
            .load(BuildConfig.BASE_URL_IMAGE + resultItem.posterPath)
            .into(holder.itemView.img_photo)
        if(resultItem.title!=null){
            holder
                .itemView
                .tv_name
                ?.text = resultItem.title
        }else{
            holder
                .itemView
                .tv_name
                ?.text = resultItem.name
        }

        holder
            .itemView
            .tv_description
            ?.text = resultItem.overview
        listFilm.add(resultItem)
    }

    override fun getItemCount(): Int = resultMovieDb.size

    fun getItem(): ArrayList<Result> = resultMovieDb

    fun refreshAdapter(resultTheMovieDb: List<Result>) {
        this.resultMovieDb.addAll(resultTheMovieDb)
        notifyItemRangeChanged(0, this.resultMovieDb.size)
    }
    fun getGenres(genreIds: List<Int>):  String {
        val movieGenres= arrayListOf<String>()
        var listGenre= String()
        for (genreId in genreIds) {
            for (genre in genreMovieDb) {
                if (genre.id == genreId) {
                    movieGenres.add(genre.name)
                    break
                }
            }
        }
        for(genre in movieGenres){
            if(movieGenres.size>1){
                listGenre+=genre+", "
            }else{
                listGenre+=genre
            }
        }
        return listGenre
    }
    inner class ViewHolderTheMovieDb(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

}