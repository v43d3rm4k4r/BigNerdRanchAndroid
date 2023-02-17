package com.bignerdranch.android.photogallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import com.bignerdranch.android.photogallery.presentation.PhotoGalleryViewModel
import com.bignerdranch.android.photogallery.ui.recyclerviewutils.PhotoAdapter
import com.bignerdranch.android.photogallery.utils.fastLazy

class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<PhotoGalleryViewModel>()
    private val adapter by fastLazy { PhotoAdapter(viewModel::onPhotoClicked) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPhotoGalleryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observePhotos()
    }

    private fun observePhotos() {
        viewModel.galleryItemsLiveData.observe(viewLifecycleOwner) { galleryItems ->
            adapter.submitList(galleryItems)
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
        _binding = null
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()

        private const val TAG = "PhotoGalleryFragment"
    }
}