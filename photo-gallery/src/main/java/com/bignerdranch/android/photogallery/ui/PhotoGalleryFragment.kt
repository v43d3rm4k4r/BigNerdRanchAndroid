package com.bignerdranch.android.photogallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import com.bignerdranch.android.photogallery.presentation.PhotoGalleryViewModel
import com.bignerdranch.android.photogallery.ui.recyclerviewutils.PhotoAdapter

import com.bignerdranch.android.androidutils.fastLazyViewModel
import com.bignerdranch.android.kotlinutils.fastLazy
import com.bignerdranch.android.androidutils.closeKeyboard
import com.bignerdranch.android.androidutils.network.ConnectivityObserver
import com.bignerdranch.android.androidutils.network.NetworkConnectivityObserver
import com.bignerdranch.android.androidutils.showSnackbar
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.data.QueryPreferences
import com.bignerdranch.android.photogallery.presentation.PhotoGallerySingleLiveEvent
import com.bignerdranch.android.photogallery.presentation.PhotoGallerySingleLiveEvent.*

class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by fastLazyViewModel {
        PhotoGalleryViewModel(
            QueryPreferences(requireContext().applicationContext),
            NetworkConnectivityObserver(requireContext().applicationContext)
        )
    }
    private val adapter by fastLazy { PhotoAdapter(viewModel::onPhotoClicked, viewModel.thumbnailDownloader::queueThumbnail) }

    private val menuProvider by fastLazy { PhotoGalleryMenuProvider(requireActivity().applicationContext,
        viewModel, this::closeKeyboard) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPhotoGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(menuProvider)
        setupUI()
        observePhotos()
        viewModel.mediator.observe(viewLifecycleOwner, ::handleEvent)
    }

    private fun handleEvent(event: PhotoGallerySingleLiveEvent) =
        with(binding) {
            when (event) {
                ShowProgressBar -> {
                    progressBar.isVisible = true
                    photoRecyclerView.isVisible = false
                    bottomTextView.isVisible = false
                }
                HideProgressBar -> {
                    progressBar.isVisible = false
                    photoRecyclerView.isVisible = true
                    bottomTextView.isVisible = false
                }
                ShowRequestError -> {
                    progressBar.isVisible = false
                    photoRecyclerView.isVisible = false
                    bottomTextView.text = getString(R.string.network_error)
                    bottomTextView.isVisible = true
                }
                is ShowResult -> {
                    photoRecyclerView.isVisible = true
                    bottomTextView.isVisible = false
                    adapter.submitList(event.galleryItems)
                    progressBar.isVisible = false
                }
                ShowEmptyResult -> {
                    photoRecyclerView.isVisible = false
                    bottomTextView.text = getText(R.string.no_photos_found)
                    bottomTextView.isVisible = true
                    progressBar.isVisible = false
                }
                is ShowNetworkStatus -> {
                    when (event.status) {
                        ConnectivityObserver.Status.LOST      -> showSnackbar(R.string.network_lost)
                        ConnectivityObserver.Status.AVAILABLE -> showSnackbar(R.string.network_available)
                        else -> {}
                    }
                }
            }
        }

    private fun observePhotos() {
        viewModel.galleryItemsLiveData.observe(viewLifecycleOwner) { galleryItems ->
            if (galleryItems.isNotEmpty()) {
                handleEvent(ShowResult(galleryItems))
            }
            else {
                handleEvent(ShowEmptyResult)
            }
        }
    }

    private fun setupUI() {
        with(binding.photoRecyclerView) {
            adapter = this@PhotoGalleryFragment.adapter

            //val callback = SimpleItemTouchHelperCallback(requireContext().resources, this@CrimeListFragment.adapter)
            //val touchHelper = ItemTouchHelper(callback)
            //touchHelper.attachToRecyclerView(this)

            //val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
            //addItemDecoration(dividerItemDecoration)

            // TODO: add itemAnimator
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.thumbnailDownloader.clearQueue()
        requireActivity().removeMenuProvider(menuProvider)
        _binding = null
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()

        private const val TAG = "PhotoGalleryFragment"
    }
}