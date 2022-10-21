package com.bignardranch.android.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bignardranch.android.criminalintent.model.Crime

class CrimesAdapter(val crimes: List<Crime>) : RecyclerView.Adapter<CrimesAdapter.CrimeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val itemId = if (viewType == 1) R.layout.list_item_crime_serious // TODO: make with ConstraintLayout
        else R.layout.list_item_crime
        val view = LayoutInflater.from(parent.context).inflate(itemId, parent, false)
        return CrimeHolder(view)
    }
    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }
    override fun getItemViewType(position: Int): Int =
        if (!crimes[position].isSolved && crimes[position].requiresPolice) 1 else 0
    override fun getItemCount(): Int = crimes.size

    class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
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
                Toast.makeText(itemView.context, itemView.context.getString(R.string.calling_the_police), Toast.LENGTH_SHORT).show()
            }
        }

        override fun onClick(v: View) =
            Toast.makeText(v.context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        //adapter.notifyItemMoved(0, adapter.itemCount - 1)
    }
}
