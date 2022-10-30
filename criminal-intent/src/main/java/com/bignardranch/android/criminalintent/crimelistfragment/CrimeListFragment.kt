package com.bignardranch.android.criminalintent.crimelistfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignardranch.android.criminalintent.R
import com.bignardranch.android.criminalintent.databinding.FragmentCrimeListBinding
import com.bignardranch.android.criminalintent.model.Crime
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    /**
     * Functional interface for crime navigation. Must be implemented by host activity.
     */
    fun interface OnCrimeSelectedListener { fun onCrimeSelected(crimeId: UUID) }

    private lateinit var binding: FragmentCrimeListBinding
    private val crimeListViewModel: CrimeListViewModel by viewModels()
    private val parent get() = activity as OnCrimeSelectedListener
    private val adapter: CrimesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        CrimesAdapter(parent, ::onCrimeClicked, ::onCallPoliceClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCrimeListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        observeCrimes()
    }

    private fun setupView() {
        with(binding.crimeRecyclerView) {
            layoutManager = LinearLayoutManager(context) // TODO: add itemAnimator
            adapter = this@CrimeListFragment.adapter
        }
    }

    private fun observeCrimes() {
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            Log.i(TAG, "Got crimes ${crimes.size}")
            adapter.submitItems(crimes)
        }
    }

    private fun onCrimeClicked(crime: Crime) {
        Toast.makeText(requireContext(), "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        //adapter.notifyItemMoved(0, adapter.itemCount - 1)
    }

    private fun onCallPoliceClicked() {
        Toast.makeText(requireContext(), getString(R.string.calling_the_police), Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(): CrimeListFragment = CrimeListFragment()
    }
}