package com.bignardranch.android.criminalintent.crimefragment.datetimefragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.bignardranch.android.criminalintent.crimefragment.CrimeFragment
import com.bignardranch.android.criminalintent.crimefragment.DIALOG_DATE_TIME_REQUEST_CODE
import java.util.*

private const val ARG_TIME = "time"
const val RESULT_TIME = "result_date"

/**
 * This time picker is hosted by [CrimeFragment].
 */
class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private lateinit var calendar: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar = Calendar.getInstance().apply {
            time = arguments?.getSerializable(ARG_TIME) as Date
        }
        val initialHour   = calendar[Calendar.HOUR]
        val initialMinute = calendar[Calendar.MINUTE]

        return TimePickerDialog(
            requireContext(),
            this,
            initialHour,
            initialMinute,
            false
        )
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar[Calendar.HOUR]   = hourOfDay
        calendar[Calendar.MINUTE] = minute
        setFragmentResult(DIALOG_DATE_TIME_REQUEST_CODE, bundleOf(RESULT_TIME to calendar.time))
    }

    companion object {

        @JvmStatic val TAG = TimePickerDialogFragment::class.java.simpleName

        fun newInstance(date: Date): TimePickerDialogFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, date)
            }
            return TimePickerDialogFragment().apply {
                arguments = args
            }
        }
    }
}