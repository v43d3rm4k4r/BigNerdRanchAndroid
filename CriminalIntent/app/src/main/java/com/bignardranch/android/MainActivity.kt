package com.bignardranch.android

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.bignardranch.android.criminalintent.CrimeListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}