package com.bignerdranch.android.photogallery.utils.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrAPI {

    @GET("/")
    fun fetchContents(): Call<String>
}