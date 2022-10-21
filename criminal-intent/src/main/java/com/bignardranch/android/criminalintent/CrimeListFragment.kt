package com.bignardranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignardranch.android.criminalintent.model.Crime

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    private val crimeListViewModel: CrimeListViewModel by viewModels()
    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimesAdapter = CrimesAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)?.apply {
            crimeRecyclerView = findViewById(R.id.crime_recycler_view)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeCrimes()
    }

    private fun setupView() {
        crimeRecyclerView.layoutManager = LinearLayoutManager(context) // TODO: add itemAnimator
        crimeRecyclerView.adapter = adapter
    }

    private fun observeCrimes() {
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            Log.i(TAG, "Got crimes ${crimes.size}")
            updateUI(crimes)
        }
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimesAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): CrimeListFragment = CrimeListFragment()
    }
}
