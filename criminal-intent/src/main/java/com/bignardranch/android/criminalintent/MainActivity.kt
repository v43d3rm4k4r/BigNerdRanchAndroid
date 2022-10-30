package com.bignardranch.android.criminalintent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignardranch.android.criminalintent.crimefragment.CrimeFragment
import com.bignardranch.android.criminalintent.crimelistfragment.CrimeListFragment
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(R.layout.activity_main),
    CrimeListFragment.OnCrimeSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = CrimeListFragment.newInstance() // TODO: add args (Crimes?)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

        // TODO: do something with currentFragment
    }

    override fun onCrimeSelected(crimeId: UUID) {
        val fragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}