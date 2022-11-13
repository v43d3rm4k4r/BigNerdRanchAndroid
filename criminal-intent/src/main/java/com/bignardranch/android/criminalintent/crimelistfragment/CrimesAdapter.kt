package com.bignardranch.android.criminalintent.crimelistfragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bignardranch.android.criminalintent.R
import com.bignardranch.android.criminalintent.contracts.Navigator
import com.bignardranch.android.criminalintent.databinding.ListItemCrimeBinding
import com.bignardranch.android.criminalintent.databinding.ListItemCrimeSeriousBinding
import com.bignardranch.android.criminalintent.model.Crime

/**
 * Part of the [CrimeListFragment].
 */

class CrimesAdapter(
    private val host: Navigator,
    private val onItemClicked: (crime: Crime) -> Unit,
    private val onCallPoliceClicked: () -> Unit
) : ListAdapter<Crime, CrimesAdapter.BaseViewHolder<ViewBinding>>(ItemCallback), View.OnClickListener {

    var crimes: List<Crime> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

//    fun submitItems(crimes: List<Crime>) {
//        this.crimes = crimes
//    }

    override fun onClick(view: View) {
        val crime = view.tag as Crime
        when (view.id) {
            R.id.list_item_crime, R.id.list_item_crime_serious -> { onItemClicked(crime); host.onCrimeSelected(crime.id) }
            R.id.call_the_police_button -> onCallPoliceClicked()
        }
    }

    // This method for setting on click listener...
    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): BaseViewHolder<ViewBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)

        view.setOnClickListener(this)

        return when (viewType) {
            ITEM_CRIME_SERIOUS -> {
                val binding = ListItemCrimeSeriousBinding.bind(view)
                binding.callThePoliceButton.setOnClickListener(this)
                SeriousCrimeHolder(binding)
            }
            else -> {
                val binding = ListItemCrimeBinding.bind(view)
                CrimeHolder(binding)
            }
        }
    }

    // ...this one is for other stuff
    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {
        holder.bind(crimes[position])
    }

    override fun getItemViewType(position: Int): Int = when {
            !crimes[position].isSolved && crimes[position].requiresPolice -> ITEM_CRIME_SERIOUS
            else -> ITEM_CRIME
        }

    override fun getItemCount(): Int = crimes.size

    abstract class BaseViewHolder<out VB : ViewBinding>(open val binding: VB)
        : RecyclerView.ViewHolder(binding.root) {
            abstract fun bind(crime: Crime)
        }

    class CrimeHolder(
        override val binding: ListItemCrimeBinding,
    ) : BaseViewHolder<ListItemCrimeBinding>(binding) {

        override fun bind(crime: Crime) {
            with(binding) {
                root.tag = crime
                crimeTitleTextView.text = crime.title
                crimeDateTextView.text = crime.date.toString()
                crimeSolvedImageView.isVisible = crime.isSolved
            }
        }
    }

    class SeriousCrimeHolder(
        override val binding: ListItemCrimeSeriousBinding,
    ) : BaseViewHolder<ListItemCrimeSeriousBinding>(binding) {

        override fun bind(crime: Crime) {
            with(binding) {
                root.tag = crime
                crimeTitleTextView.text = crime.title
                crimeDateTextView.text = crime.date.toString()
            }
        }
    }

    object ItemCallback : DiffUtil.ItemCallback<Crime>() {
        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean =
            oldItem == newItem
    }

    private companion object {
        @LayoutRes const val ITEM_CRIME = R.layout.list_item_crime
        @LayoutRes const val ITEM_CRIME_SERIOUS = R.layout.list_item_crime_serious
    }
}