package com.bignardranch.android.criminalintent.contracts

import androidx.fragment.app.Fragment
import java.util.*
import com.bignardranch.android.criminalintent.MainActivity

fun Fragment.navigator(): Navigator = requireActivity() as Navigator

/**
 * Implemented by host activity ([MainActivity]).
 */
fun interface Navigator {
    fun onCrimeSelected(crimeId: UUID)
}