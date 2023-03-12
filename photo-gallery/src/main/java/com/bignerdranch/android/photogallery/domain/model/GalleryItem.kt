package com.bignerdranch.android.photogallery.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GalleryItem(
    val title: String = "",
    val id:    String = "",
    @SerializedName("url_s") val url: String = ""
)