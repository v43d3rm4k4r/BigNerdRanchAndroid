package com.bignerdranch.android.criminalintent

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.bignerdranch.android.criminalintent.contracts.Navigator
import com.bignerdranch.android.criminalintent.crimefragment.CrimeFragment
import com.bignerdranch.android.criminalintent.crimelistfragment.CrimeListFragment

import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main), Navigator {

//    private val currentFragment: Fragment by lazy(LazyThreadSafetyMode.NONE) {
//        supportFragmentManager.findFragmentById(R.id.fragment_container)!!
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Needed only if `FragmentContainerView` with `android:name tag` is not used:
        if (savedInstanceState == null) {
            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
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