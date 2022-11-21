package com.bignardranch.android.criminalintent.crimefragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bignardranch.android.criminalintent.databinding.FragmentCrimeBinding
import com.bignardranch.android.criminalintent.crimefragment.datetimefragments.DatePickerDialogFragment
import com.bignardranch.android.criminalintent.crimefragment.datetimefragments.RESULT_DATE
import com.bignardranch.android.criminalintent.crimefragment.datetimefragments.RESULT_TIME
import com.bignardranch.android.criminalintent.crimefragment.datetimefragments.TimePickerDialogFragment
import com.bignardranch.android.criminalintent.model.Crime
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_CRIME_ID = "crime_id"

const val DIALOG_DATE_TIME_REQUEST_CODE = "Dialog date time result"

class CrimeFragment : Fragment() {

    private lateinit var binding: FragmentCrimeBinding
    private lateinit var crime: Crime
    private val crimeViewModel: CrimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrimeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeViewModel.crimeLiveData.observe(
            viewLifecycleOwner
        ) { crime ->
            crime?.let {
                this.crime = crime
                updateUI()
            }
        }

        with(binding) {
            crimeTitleTextView.doOnTextChanged { text, _, _, _ -> crime = crime.copy(title = text.toString()) }
            crimeSolvedCheckbox.setOnCheckedChangeListener { _, isChecked -> crime = crime.copy(isSolved = isChecked) }
            crimeDateButton.setOnClickListener { // FIXME: Если при нажатии на DialogFragment, выборе времени (ОК не нажимать) и повороте экрана нажать ОК, то время не обновится. Если нажать второй раз - обновиться на кнопке, но в самом циферблате будет старое.
                showDatePickerDialogFragment()
                setupDatePickerDialogFragmentListener()
            }
            crimeTimeButton.setOnClickListener {
                showTimePickerDialogFragment()
                setupTimePickerDialogFragmentListener()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeViewModel.saveCrime(crime)
    }

    private fun updateDate(date: Date) {
        crime = crime.copy(date = date)
        updateUI()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateUI() {
        with(binding) {
            crimeTitleTextView.setText(crime.title)

            val calendar = Calendar.getInstance().apply { time = crime.date }
            val dateFormat = SimpleDateFormat("EEEE, d MMMM, yyyy")
            val timeFormat = SimpleDateFormat("h:mm a")
            crimeDateButton.text = dateFormat.format(calendar.time)
            crimeTimeButton.text = timeFormat.format(calendar.time)

            crimeSolvedCheckbox.isChecked = crime.isSolved
            crimeSolvedCheckbox.jumpDrawablesToCurrentState() // cancelling checkbox animation
        }
    }

    private fun showDatePickerDialogFragment() {
        DatePickerDialogFragment.newInstance(crime.date).apply {
            //setTargetFragment(this@CrimeFragment, DIALOG_REQUEST_CODE) // used in book, deprecated, use setFragmentResultListener()
            show(this@CrimeFragment.parentFragmentManager, DatePickerDialogFragment.TAG)
        }
    }

    private fun setupDatePickerDialogFragmentListener() {
        parentFragmentManager.setFragmentResultListener(DIALOG_DATE_TIME_REQUEST_CODE, viewLifecycleOwner) { requestKey, result ->
            if (requestKey == DIALOG_DATE_TIME_REQUEST_CODE) {
                val date = result.getSerializable(RESULT_DATE) as Date
                updateDate(date)
            }
        }
    }

    private fun showTimePickerDialogFragment() {
        TimePickerDialogFragment.newInstance(crime.date).apply {
            show(this@CrimeFragment.parentFragmentManager, TimePickerDialogFragment.TAG)
        }
    }

    private fun setupTimePickerDialogFragmentListener() {
        parentFragmentManager.setFragmentResultListener(DIALOG_DATE_TIME_REQUEST_CODE, viewLifecycleOwner) { requestKey, result ->
            if (requestKey == DIALOG_DATE_TIME_REQUEST_CODE) {
                val date = result.getSerializable(RESULT_TIME) as Date
                updateDate(date)
            }
        }
    }

    companion object {

        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }
}