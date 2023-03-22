package com.bignerdranch.android.photogallery.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.bignerdranch.android.photogallery.R

class PhotoGalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, PhotoGalleryActivity::class.java)
    }
}