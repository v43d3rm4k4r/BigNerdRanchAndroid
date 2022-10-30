package com.bignardranch.android.criminalintent.crimelistfragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bignardranch.android.criminalintent.R
import com.bignardranch.android.criminalintent.model.Crime

/**
 * Part of the [CrimeListFragment].
 */
class CrimesAdapter(
    private val parent: CrimeListFragment.OnCrimeSelectedListener,
    private val onItemClicked: (crime: Crime) -> Unit,
    private val onCallPoliceClicked: () -> Unit
) : RecyclerView.Adapter<CrimesAdapter.CrimeHolder>() {

    private var crimes: List<Crime> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitItems(crimes: List<Crime>) {
        this.crimes = crimes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)
        return CrimeHolder(view, onItemClicked, onCallPoliceClicked)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        holder.bind(crimes[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            !crimes[position].isSolved && crimes[position].requiresPolice -> R.layout.list_item_crime_serious
            else -> R.layout.list_item_crime
        }
    }

    override fun getItemCount(): Int = crimes.size

    inner class CrimeHolder(
        view: View,
        private val onItemClicked: (crime: Crime) -> Unit,
        private val onCallPoliceClicked: () -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title_text_view)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date_text_view)
        private val solvedImageView: ImageView? = itemView.findViewById(R.id.crime_solved_image_view)
        private val callThePoliceButton: Button? = itemView.findViewById(R.id.call_the_police_button)

        fun bind(crime: Crime) {
            itemView.setOnClickListener { onItemClicked(crime); parent.onCrimeSelected(crime.id) }
            callThePoliceButton?.setOnClickListener { onCallPoliceClicked() }
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()
            solvedImageView?.isVisible = crime.isSolved
        }
    }
}