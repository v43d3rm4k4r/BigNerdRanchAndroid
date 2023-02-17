package com.bignerdranch.android.photogallery.utils.flickrapi

import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickrAPI {

    @GET("services/rest/?method=flickr.interestingness.getList&api_key=e362e26e424002430b3ff34146148179&format=json&nojsoncallback=1&extras=url_s")
    fun fetchPhotos(): Call<FlickrResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}