package com.bignardranch.android.criminalintent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignardranch.android.criminalintent.crimelistfragment.CrimeListFragment
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main), CrimeListFragment.Callbacks {

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
        TODO("Not yet implemented")
    }
}