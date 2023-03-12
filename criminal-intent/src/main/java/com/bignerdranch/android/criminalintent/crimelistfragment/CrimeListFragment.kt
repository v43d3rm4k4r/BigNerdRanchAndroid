package com.bignerdranch.android.criminalintent.crimelistfragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout

import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper

import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.contracts.navigator
import com.bignerdranch.android.criminalintent.crimelistfragment.recyclerviewutils.CrimesAdapter
import com.bignerdranch.android.criminalintent.crimelistfragment.recyclerviewutils.SimpleItemTouchHelperCallback
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import com.bignerdranch.android.criminalintent.model.Crime

import com.bignerdranch.android.kotlinutils.fastLazy
import com.bignerdranch.android.androidutils.showToast

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding: FragmentCrimeListBinding
        get() = _binding!!
    private val crimeListViewModel: CrimeListViewModel by viewModels()
    private val adapter by fastLazy {
        CrimesAdapter(navigator(), ::onCrimeClicked, ::onCallPoliceClicked, ::onCrimeSwiped, ::onCrimeMoved)
    }
    private val menuProvider by fastLazy { CrimeListMenuProvider(navigator(), crimeListViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCrimeListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(menuProvider)
        setupView()
        observeCrimes()
    }

    private fun setupView() {
        with(binding.crimeRecyclerView) {
            adapter = this@CrimeListFragment.adapter

            val callback = SimpleItemTouchHelperCallback(requireContext().resources, this@CrimeListFragment.adapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)

            val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
            addItemDecoration(dividerItemDecoration)

            // TODO: add itemAnimator
        }
    }

    private fun observeCrimes() {
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            Log.i(TAG, "Got crimes ${crimes.size}")
            binding.crimeRecyclerView.isVisible = crimes.isNotEmpty()
            binding.noUsersTextView.isVisible   = crimes.isEmpty()
            adapter.submitList(crimes.reversed())
        }
    }

    private fun onCrimeClicked(crime: Crime) = showToast("${crime.title} pressed!")

    private fun onCallPoliceClicked() = showToast(R.string.calling_the_police)

    private fun onCrimeSwiped(crime: Crime) = crimeListViewModel.deleteCrime(crime)

    private fun onCrimeMoved(fromPosition: Int, toPosition: Int) {
        TODO("Not needed in this app, for now")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
        _binding = null
    }

    companion object {

        private const val TAG = "CrimeListFragment"

        fun newInstance(): CrimeListFragment = CrimeListFragment()
    }
}