package com.bignerdranch.android.criminalintent.crimelistfragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast

import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager

import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.contracts.navigator
import com.bignerdranch.android.criminalintent.crimelistfragment.recyclerviewutils.CrimesAdapter
import com.bignerdranch.android.criminalintent.crimelistfragment.recyclerviewutils.SimpleItemTouchHelperCallback
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import com.bignerdranch.android.criminalintent.model.Crime

import kotlin.LazyThreadSafetyMode.NONE

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    private lateinit var binding: FragmentCrimeListBinding
    private val crimeListViewModel: CrimeListViewModel by viewModels()
    private val adapter by lazy(NONE) {
        CrimesAdapter(navigator(), ::onCrimeClicked, ::onCallPoliceClicked, ::onCrimeSwiped, ::onCrimeMoved)
    }
    private val menuProvider by lazy(NONE) { CrimeListMenuProvider(navigator(), crimeListViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCrimeListBinding.inflate(layoutInflater)
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

            val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)

            // TODO: add itemAnimator
        }
    }

    private fun observeCrimes() {
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            Log.i(TAG, "Got crimes ${crimes.size}")
            binding.crimeRecyclerView.isVisible = crimes.isNotEmpty()
            binding.noUsersTextView.isVisible   = crimes.isEmpty()
            adapter.submitList(crimes)
        }
    }

    private fun onCrimeClicked(crime: Crime) = showToast("${crime.title} pressed!")

    private fun onCallPoliceClicked() = showToast(R.string.calling_the_police)

    private fun onCrimeSwiped(crime: Crime) = crimeListViewModel.deleteCrime(crime)

    private fun onCrimeMoved(fromPosition: Int, toPosition: Int) {
        TODO("No needed in this app, for now")
    }

    private fun showToast(@StringRes resId: Int) = showToast(getString(resId))

    private fun showToast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
    }

    companion object {
        fun newInstance(): CrimeListFragment = CrimeListFragment()
    }
}
