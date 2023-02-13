package com.bignerdranch.android.photogallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import com.bignerdranch.android.photogallery.utils.api.FlickrAPI
import retrofit2.Retrofit

class PhotoGalleryFragment : Fragment() {

    private lateinit var binding: FragmentPhotoGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.flickr.com/")
            .build()

        val flickrAPI = retrofit.create(FlickrAPI::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPhotoGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }
}