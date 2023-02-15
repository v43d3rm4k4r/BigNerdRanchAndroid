package com.bignerdranch.android.photogallery.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import com.bignerdranch.android.photogallery.domain.FlickrFetcher

class PhotoGalleryFragment : Fragment() {

    private lateinit var binding: FragmentPhotoGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flickrLiveData = FlickrFetcher().fetchPhotos() // TODO: move this stuff to ViewModel (presentation)
        flickrLiveData.observe(this) { responseGalleryItem ->
            Log.d(TAG, "Response received: $responseGalleryItem")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPhotoGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()

        private const val TAG = "PhotoGalleryFragment"
    }
}