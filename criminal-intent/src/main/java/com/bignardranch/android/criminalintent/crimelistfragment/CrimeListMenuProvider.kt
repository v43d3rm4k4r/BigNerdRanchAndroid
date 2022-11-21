package com.bignardranch.android.criminalintent.crimelistfragment

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.bignardranch.android.criminalintent.R
import com.bignardranch.android.criminalintent.contracts.Navigator
import com.bignardranch.android.criminalintent.model.Crime

/**
 * Menu provider used by [CrimeListFragment].
 */
class CrimeListMenuProvider(private val host: Navigator,
                            private val crimeListViewModel: CrimeListViewModel)
    : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.fragment_crime_list, menu)

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
        R.id.new_crime -> {
            val crime = Crime()
            crimeListViewModel.addCrime(crime)
            host.onCrimeSelected(crime.id)
            true
        }
        else -> false
    }
}