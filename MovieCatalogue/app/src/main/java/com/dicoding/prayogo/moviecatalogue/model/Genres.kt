package com.dicoding.prayogo.moviecatalogue.model


import com.google.gson.annotations.SerializedName

data class Genres(
    @SerializedName("genres")
    val genres: List<Genre>
)