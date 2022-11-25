package com.bignardranch.android.criminalintent.crimefragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.*
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bignardranch.android.criminalintent.R
import com.bignardranch.android.criminalintent.databinding.FragmentCrimeBinding
import com.bignardranch.android.criminalintent.crimefragment.datetimefragments.DatePickerDialogFragment
import com.bignardranch.android.criminalintent.crimefragment.datetimefragments.DatePickerDialogFragment.Companion.RESULT_DATE
import com.bignardranch.android.criminalintent.crimefragment.datetimefragments.TimePickerDialogFragment
import com.bignardranch.android.criminalintent.crimefragment.datetimefragments.TimePickerDialogFragment.Companion.RESULT_TIME
import com.bignardranch.android.criminalintent.model.Crime
import java.text.SimpleDateFormat
import java.util.*

class CrimeFragment : Fragment() {

    private lateinit var binding: FragmentCrimeBinding
    private lateinit var crime: Crime
    private val crimeViewModel: CrimeViewModel by viewModels()
    private val contactsActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK || result?.data?.data == null)
            return@registerForActivityResult
        val contactURI = result.data?.data!!
        // Specify for which fields the query should return values:
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val cursor = requireActivity().contentResolver
            .query(contactURI, queryFields, null, null, null)
        cursor?.use {
            if (it.count == 0) return@registerForActivityResult
            it.moveToFirst()
            val suspectStr = it.getString(0)
            crime = crime.copy(suspect = suspectStr)
            crimeViewModel.saveCrime(crime)
            binding.crimeChooseSuspectButton.text = getString(R.string.crime_suspect_text, suspectStr)
        }
    }

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

    @SuppressLint("Range")
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
            crimeDateButton.setOnClickListener {
                showDatePickerDialogFragment()
            }
            crimeTimeButton.setOnClickListener {
                showTimePickerDialogFragment()
            }

            crimeChooseSuspectButton.apply {
                val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                val options = ActivityOptionsCompat.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                setOnClickListener {
                    contactsActivityLauncher.launch(pickContactIntent, options)
                }
                // If there is no phonebook application:
                val packageManager = requireActivity().packageManager
                val resolverActivity = packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
                if (resolverActivity == null) {
                    isEnabled = false
                }
            }

            crimeSendReportButton.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT,    getCrimeReport())
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                }.also { intent ->
                    // For some reason Android using getCrimeReport() result for chooser title,
                    // but not R.string.send_report
//                    val chooserIntent = Intent.createChooser(intent, getString(R.string.send_report))
//                    startActivity(chooserIntent)
                      startActivity(intent)
                }
            }

            crimeCallSuspectButton.setOnClickListener {
                // Getting phone number by name (crime.suspect):
                val resolver = requireActivity().contentResolver
                val contactsCursor = resolver.query(
                        ContactsContract.Contacts.CONTENT_URI, null,
                        null, null, null
                    )
                if (contactsCursor == null || !contactsCursor.moveToFirst()) return@setOnClickListener
                contactsCursor.use {
                    val contactId = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                    val phonesCursor = resolver.query(
                        Phone.CONTENT_URI, null,
                        Phone.CONTACT_ID + " = " + contactId,
                        null, null
                    );
                    phonesCursor?.use {
                        while (it.moveToNext()) {
                            val numberStr = it.getString(it.getColumnIndex(Phone.NUMBER));
                            when ( it.getInt(it.getColumnIndex(Phone.TYPE)) ) {
                                Phone.TYPE_HOME -> {}
                                Phone.TYPE_MOBILE -> {}
                                Phone.TYPE_WORK -> {}
                            }
                        }
                    }
                }

                // TODO: start dial activity
//                Intent(Intent.ACTION_DIAL).apply {
//                    //data = Uri.parse(phoneStr)
//                }.also { intent ->
//                    startActivity(intent)
//                }
            }
        }
        setupDatePickerDialogFragmentListener()
        setupTimePickerDialogFragmentListener()
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

            if (crime.suspect.isNotEmpty()) {
                crimeChooseSuspectButton.text = getString(R.string.crime_suspect_text, crime.suspect)
                crimeCallSuspectButton.text   = getString(R.string.crime_call_suspect_text)
                crimeCallSuspectButton.isEnabled = true
            } else {
                crimeChooseSuspectButton.text = getString(R.string.crime_call_suspect_disabled_text)
                crimeCallSuspectButton.isEnabled = false
            }
        }
    }

    private fun showDatePickerDialogFragment() {
        DatePickerDialogFragment.newInstance(crime.date).apply {
            //setTargetFragment(this@CrimeFragment, DIALOG_REQUEST_CODE) // used in book, deprecated, use setFragmentResultListener()
            show(this@CrimeFragment.parentFragmentManager, DatePickerDialogFragment.TAG)
        }
    }

    private fun setupDatePickerDialogFragmentListener() { // TODO: Can be moved to corresponding DialogFragment
        parentFragmentManager.setFragmentResultListener(REQUEST_DIALOG_DATE_TIME, this) { requestKey, result ->
            if (requestKey == REQUEST_DIALOG_DATE_TIME) {
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

    private fun setupTimePickerDialogFragmentListener() { // TODO: Can be moved to corresponding DialogFragment
        parentFragmentManager.setFragmentResultListener(REQUEST_DIALOG_DATE_TIME, this) { requestKey, result ->
            if (requestKey == REQUEST_DIALOG_DATE_TIME) {
                val date = result.getSerializable(RESULT_TIME) as Date
                updateDate(date)
            }
        }
    }

    private fun getCrimeReport(): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspect = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }

        return getString(R.string.crime_report, crime.title, dateString, solvedString, suspect)
    }

    companion object {

        private const val ARG_CRIME_ID = "crime_id"
        private const val DATE_FORMAT  = "EEE, MMM, dd"
        @JvmStatic    val REQUEST_DIALOG_DATE_TIME = "Dialog date time result"

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