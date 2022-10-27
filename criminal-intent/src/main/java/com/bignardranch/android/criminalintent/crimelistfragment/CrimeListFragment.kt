package com.bignardranch.android.criminalintent.crimelistfragment

import android.content.Context
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignardranch.android.criminalintent.R
import com.bignardranch.android.criminalintent.model.Crime
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment(R.layout.fragment_crime_list) {

    /**
     * Must be implemented by host activity.
     */
    fun interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private lateinit var crimeRecyclerView: RecyclerView
    private val crimeListViewModel: CrimeListViewModel by viewModels()
    private val adapter: CrimesAdapter = CrimesAdapter(::onCrimeClicked, ::onCallPoliceClicked)
    private val callbacks get() = activity as Callbacks

    private val _seconds = MutableLiveData<Int>()
    val seconds: LiveData<Int> get() = _seconds

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