package com.bignerdranch.android.photogallery.data.flickrapi

import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrAPI {

    /**
     * Fetches interesting photos for today.
     */
    @GET("services/rest?method=flickr.interestingness.getList")
    fun fetchInterestingPhotos(): Call<FlickrResponseJSON>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    @GET("services/rest?method=flickr.photos.search")
    fun searchPhotos(@Query("text") query: String): Call<FlickrResponseJSON>
}