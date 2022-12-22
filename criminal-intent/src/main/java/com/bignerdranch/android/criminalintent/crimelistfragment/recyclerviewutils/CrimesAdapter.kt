package com.bignerdranch.android.criminalintent.crimelistfragment.recyclerviewutils

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

import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.contracts.Navigator
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeBinding
import com.bignerdranch.android.criminalintent.databinding.ListItemCrimeSeriousBinding
import com.bignerdranch.android.criminalintent.model.Crime
import java.text.SimpleDateFormat
import java.util.*

/**
 * Part of the [CrimeListFragment].
 */
class CrimesAdapter(
    private val host: Navigator,
    private val onItemClicked:       (crime: Crime) -> Unit,
    private val onCallPoliceClicked: () -> Unit,
    private val onCrimeSwiped:       (crime: Crime) -> Unit,
    private val onCrimeMoved:        (fromPosition: Int, toPosition: Int) -> Unit
) : ListAdapter<Crime, CrimesAdapter.BaseViewHolder>(ItemCallback),
    View.OnClickListener,
    ItemTouchHelperAdapter {

    override fun onClick(view: View) {
        val crime = view.tag as Crime
        when (view.id) {
            R.id.list_item_crime, R.id.list_item_crime_serious -> { onItemClicked(crime); host.onCrimeSelected(crime.id) }
            R.id.call_the_police_button -> onCallPoliceClicked()
        }
    }

    // This method for setting on click listener...
    override fun onCreateViewHolder(parent: ViewGroup, @LayoutRes viewType: Int): BaseViewHolder {
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
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return when {
            !currentItem.isSolved && currentItem.requiresPolice -> ITEM_CRIME_SERIOUS
            else -> ITEM_CRIME
        }
    }

    /**
     * [ItemTouchHelperAdapter] interface implementations:
     */
    override fun onItemMove(fromPosition: Int, toPosition: Int) = onCrimeMoved(fromPosition, toPosition)
    override fun onItemDismiss(position: Int)                   = onCrimeSwiped(getItem(position))

    /**
     * ViewHolders implementations:
     */
    abstract class BaseViewHolder(binding: ViewBinding)
        : RecyclerView.ViewHolder(binding.root) {

            abstract fun bind(crime: Crime)

            @SuppressLint("SimpleDateFormat")
            protected fun Date.toFormattedString(): String {
                val dateFormat = SimpleDateFormat("d MMMM yyyy, HH:mm")
                val calendar   = Calendar.getInstance().apply { time = this@toFormattedString }
                return dateFormat.format(calendar.time)
            }
        }

    class CrimeHolder(
        private val binding: ListItemCrimeBinding,
    ) : BaseViewHolder(binding) {

        override fun bind(crime: Crime) {
            with(binding) {
                root.tag = crime
                crimeTitleTextView.text        = crime.title
                crimeDateTextView.text         = crime.date.toFormattedString()
                crimeSolvedImageView.isVisible = crime.isSolved
            }
        }
    }

    class SeriousCrimeHolder(
        private val binding: ListItemCrimeSeriousBinding,
    ) : BaseViewHolder(binding) {

        override fun bind(crime: Crime) {
            with(binding) {
                root.tag = crime
                callThePoliceButton.tag = crime
                crimeTitleTextView.text = crime.title
                crimeDateTextView.text  = crime.date.toFormattedString()
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
        @LayoutRes const val ITEM_CRIME         = R.layout.list_item_crime
        @LayoutRes const val ITEM_CRIME_SERIOUS = R.layout.list_item_crime_serious
    }
}