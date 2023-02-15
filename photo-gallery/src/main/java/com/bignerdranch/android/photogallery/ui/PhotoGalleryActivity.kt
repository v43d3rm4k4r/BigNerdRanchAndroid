package com.bignerdranch.android.photogallery.ui

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle

import com.bignerdranch.android.photogallery.R

class PhotoGalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)
    }
}