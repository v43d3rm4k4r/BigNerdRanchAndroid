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

class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by fastLazyViewModel { PhotoGalleryViewModel(requireActivity().application) }
    private val adapter by fastLazy { PhotoAdapter(viewModel::onPhotoClicked, viewModel.thumbnailDownloader::queueThumbnail) }

    private val menuProvider by fastLazy { PhotoGalleryMenuProvider(viewModel, this::closeKeyboard) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPhotoGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(menuProvider)
        setupUI()
        observePhotos()
    }

    private fun observePhotos() {
        viewModel.galleryItemsLiveData.observe(viewLifecycleOwner) { galleryItems ->
            with(binding) {
                if (galleryItems.isNotEmpty()) {
                    photoRecyclerView.isVisible = true
                    noPhotosFoundTextView.isVisible = false
                    adapter.submitList(galleryItems)
                }
                else {
                    with(binding) {
                        photoRecyclerView.isVisible = false
                        noPhotosFoundTextView.isVisible = true
                    }
                }
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