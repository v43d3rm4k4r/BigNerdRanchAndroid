package com.bignardranch.android.criminalintent.crimefragment.datetimefragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.bignardranch.android.criminalintent.crimefragment.CrimeFragment.*
import com.bignardranch.android.criminalintent.crimefragment.CrimeFragment.Companion.DIALOG_DATE_TIME_REQUEST_CODE
import java.util.*

/**
 * This date picker is hosted by [CrimeFragment].
 */
class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var calendar: Calendar

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        calendar = Calendar.getInstance().apply {
            time = arguments?.getSerializable(ARG_DATE) as Date
        }

        val initialYear  = calendar[Calendar.YEAR]
        val initialMonth = calendar[Calendar.MONTH]
        val initialDay   = calendar[Calendar.DAY_OF_MONTH]

        return DatePickerDialog(
            requireContext(),
            this,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val resultDate = Calendar.getInstance().apply {
            this[Calendar.YEAR]         = year
            this[Calendar.MONTH]        = month
            this[Calendar.DAY_OF_MONTH] = dayOfMonth
            this[Calendar.HOUR]         = calendar[Calendar.HOUR]
            this[Calendar.MINUTE]       = calendar[Calendar.MINUTE]
        }.time
        setFragmentResult(DIALOG_DATE_TIME_REQUEST_CODE, bundleOf(RESULT_DATE to resultDate))
    }

    companion object {

        @JvmStatic val TAG = DatePickerDialogFragment::class.java.simpleName
        @JvmStatic val RESULT_DATE = "result_date"
        private const val ARG_DATE = "date"

        fun newInstance(date: Date): DatePickerDialogFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }
            return DatePickerDialogFragment().apply {
                arguments = args
            }
        }
    }
}