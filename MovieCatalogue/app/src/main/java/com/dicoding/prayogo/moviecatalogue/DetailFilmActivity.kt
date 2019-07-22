package com.dicoding.prayogo.moviecatalogue

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dicoding.prayogo.moviecatalogue.model.Film

class DetailFilmActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_FILM="extra_film"
    }
    private var imgPhoto: ImageView?=null
    private var tvName: TextView?=null
    private var tvReleaseDate: TextView?=null
    private var tvGenre: TextView?=null
    private var tvPopularity: TextView?=null
    private var tvRating: TextView?=null
    private var tvDescription: TextView?=null
    private var film=Film()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
        if(savedInstanceState!=null){
            film=savedInstanceState.getParcelable("Film")
        }else{
            film=intent.getParcelableExtra(EXTRA_FILM)
        }
        init()
        imgPhoto?.let {
            Glide
                .with(applicationContext)
                .load(BuildConfig.BASE_URL_IMAGE + film.photo)
                .into(it)
        }
        tvName?.text=film.name
        tvReleaseDate?.text=film.releaseDate
        tvGenre?.text=film.genre
        tvPopularity?.text=film.popularity.toString()
        tvRating?.text=film.rating.toString()
        tvDescription?.text=film.description
        supportActionBar?.title=film.name
    }
    @Override
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.clear()
        outState?.putParcelable("Film",film)
    }
    private fun init(){
        imgPhoto=findViewById(R.id.img_photo)
        tvName=findViewById(R.id.tv_name)
        tvReleaseDate=findViewById(R.id.tv_release_date)
        tvGenre=findViewById(R.id.tv_genre)
        tvPopularity=findViewById(R.id.tv_popularity)
        tvRating=findViewById(R.id.tv_rating)
        tvDescription=findViewById(R.id.tv_description)
    }
}
