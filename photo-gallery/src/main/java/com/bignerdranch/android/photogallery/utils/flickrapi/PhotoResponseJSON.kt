package com.bignerdranch.android.photogallery.utils.flickrapi

import com.bignerdranch.android.photogallery.domain.model.GalleryItem

import com.google.gson.annotations.SerializedName

/**
 * Represents a `photos` JSON object.
 */
class PhotoResponseJSON {

    @SerializedName("photo")
    var galleryItems: List<GalleryItem>? = null
}