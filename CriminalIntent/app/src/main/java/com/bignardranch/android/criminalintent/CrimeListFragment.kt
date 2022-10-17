package com.bignardranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.isVisible

import com.bignardranch.android.R

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private val crimeListViewModel: CrimeListViewModel by viewModels()
    private lateinit var crimeRecyclerView: RecyclerView
    private lateinit var adapter: CrimesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        crimeRecyclerView.layoutManager = LinearLayoutManager(context) // TODO: add itemAnimator
        updateUI()
        return view
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        init { itemView.setOnClickListener(this) }
        private lateinit var crime: Crime
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title_text_view)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date_text_view)
        private val solvedImageView: ImageView? = itemView.findViewById(R.id.crime_solved_image_view)
        private val callThePoliceButton: Button? = itemView.findViewById(R.id.call_the_police_button)

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            solvedImageView?.let { it.isVisible = crime.isSolved }
            if (crime.requiresPolice) callThePoliceButton?.setOnClickListener {
                Toast.makeText(context, resources.getString(R.string.calling_the_police), Toast.LENGTH_SHORT).show()
            }
        }

        override fun onClick(v: View) =
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
            //adapter.notifyItemMoved(0, adapter.itemCount - 1)
    }

    private inner class CrimesAdapter(val crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val itemId = if (viewType == 1) R.layout.list_item_crime_serious // TODO: make with ConstraintLayout
            else R.layout.list_item_crime
            val view = layoutInflater.inflate(itemId, parent, false)
            return CrimeHolder(view)
        }
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }
        override fun getItemViewType(position: Int): Int =
            if (!crimes[position].isSolved && crimes[position].requiresPolice) 1 else 0
        override fun getItemCount(): Int = crimes.size
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimesAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): CrimeListFragment = CrimeListFragment()
    }
}