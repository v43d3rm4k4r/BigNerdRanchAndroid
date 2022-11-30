package com.bignerdranch.android.criminalintent.crimelistfragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.LazyThreadSafetyMode.NONE
import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.contracts.navigator
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeListBinding
import com.bignerdranch.android.criminalintent.model.Crime

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    private lateinit var binding: FragmentCrimeListBinding
    private val crimeListViewModel: CrimeListViewModel by viewModels()
    private val adapter by lazy(NONE) { CrimesAdapter(navigator(), ::onCrimeClicked, ::onCallPoliceClicked) }
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
            layoutManager = LinearLayoutManager(context) // TODO: add itemAnimator
            adapter = this@CrimeListFragment.adapter
        }
    }

    private fun observeCrimes() {
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            Log.i(TAG, "Got crimes ${crimes.size}")
            adapter.submitList(crimes)
        }
    }

    private fun onCrimeClicked(crime: Crime) {
        Toast.makeText(requireContext(), "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        //adapter.notifyItemMoved(0, adapter.itemCount - 1)
    }

    private fun onCallPoliceClicked() {
        Toast.makeText(requireContext(), getString(R.string.calling_the_police), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
    }

    companion object {
        fun newInstance(): CrimeListFragment = CrimeListFragment()
    }
}
