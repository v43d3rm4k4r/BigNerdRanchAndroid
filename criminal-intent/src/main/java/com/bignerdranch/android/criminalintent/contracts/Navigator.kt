package com.bignerdranch.android.criminalintent.contracts

import androidx.fragment.app.Fragment
import java.util.*
import com.bignerdranch.android.criminalintent.MainActivity

fun Fragment.navigator(): Navigator = requireActivity() as Navigator

/**
 * Main fragment navigation callbacks implemented by host activity ([MainActivity]).
 */
fun interface Navigator {
    fun onCrimeSelected(crimeId: UUID)
}