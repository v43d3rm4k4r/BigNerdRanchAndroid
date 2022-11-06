package com.bignardranch.android.criminalintent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
import com.bignardranch.android.criminalintent.contracts.Navigator
import com.bignardranch.android.criminalintent.crimefragment.CrimeFragment
import com.bignardranch.android.criminalintent.crimelistfragment.CrimeListFragment
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main), Navigator {

//    private val currentFragment: Fragment by lazy(LazyThreadSafetyMode.NONE) {
//        supportFragmentManager.findFragmentById(R.id.fragment_container)!!
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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