package com.dicoding.prayogo.moviecatalogue.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film (
    var photo:String?=null,
    var name:String?=null,
    var description:String?=null,
    var releaseDate:String?=null,
    var rating:Double?=null,
    var genre:String?=null,
    var popularity:Double?=null): Parcelable