package com.bignerdranch.android.criminalintent.crimefragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.*
import android.provider.ContactsContract.Contacts
import android.provider.MediaStore
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bignerdranch.android.androidutils.showToast

import com.bignerdranch.android.criminalintent.R
import com.bignerdranch.android.criminalintent.crimefragment.datetimefragments.DatePickerDialogFragment
import com.bignerdranch.android.criminalintent.crimefragment.datetimefragments.DatePickerDialogFragment.Companion.RESULT_DATE
import com.bignerdranch.android.criminalintent.crimefragment.datetimefragments.TimePickerDialogFragment
import com.bignerdranch.android.criminalintent.crimefragment.datetimefragments.TimePickerDialogFragment.Companion.RESULT_TIME
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeBinding
import com.bignerdranch.android.criminalintent.model.Crime
import com.bignerdranch.android.criminalintent.utils.getScaledBitmap

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CrimeFragment : Fragment() {

    private var _binding: FragmentCrimeBinding? = null
    private val binding:  FragmentCrimeBinding
        get() = _binding!!
    private lateinit var crime: Crime
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private val crimeViewModel: CrimeViewModel by viewModels()
    private val contactsActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK || result?.data?.data == null)
            return@registerForActivityResult
        val contactURI = result.data?.data!!
        // Specify for which fields the query should return values:
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        val cursor = requireActivity().contentResolver
            .query(contactURI, queryFields, null, null, null)
        cursor?.use {
            if (it.count == 0) return@registerForActivityResult
            it.moveToFirst()
            val suspectStr = it.getString(0)
            crime = crime.copy(suspect = suspectStr)
            crimeViewModel.updateCrime(crime)
            binding.crimeChooseSuspectButton.text = getString(R.string.crime_suspect_text, suspectStr)
        }
    }

    private val cameraActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            updatePhotoView()
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
        _binding = FragmentCrimeBinding.inflate(layoutInflater)
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
                photoFile = crimeViewModel.getPhotoFile(crime)
                photoUri = FileProvider.getUriForFile(requireActivity(),
                    "com.bignerdranch.android.criminalintent.fileprovider",
                photoFile)
                updateUI()
                binding.progressBar.isVisible = false
            }
        }

        val packageManager = requireActivity().packageManager

        with(binding) {

            // TODO: crimePhoto.setOnClickListener {} // add zoom in/out

            crimeTitleTextView.doOnTextChanged { text, _, _, _ -> crime = crime.copy(title = text.toString()) }
            crimeSolvedCheckbox.setOnCheckedChangeListener  { _, isChecked ->
                crime = crime.copy(isSolved = isChecked, requiresPolice = !isChecked)
                crimeSeriousCheckbox.isVisible = !isChecked
                crimeSeriousCheckbox.isChecked = crime.requiresPolice
            }

            crimeSeriousCheckbox.apply {
                isVisible = !crimeSolvedCheckbox.isChecked
                isChecked = crime.requiresPolice
                setOnCheckedChangeListener { _, isChecked ->
                    crime = crime.copy(requiresPolice = isChecked)
                }
            }

            crimeDateButton.setOnClickListener {
                showDatePickerDialogFragment()
            }

            crimeTimeButton.setOnClickListener {
                showTimePickerDialogFragment()
            }

            crimeChooseSuspectButton.apply {
                val pickContactIntent = Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI)
                val options = ActivityOptionsCompat.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                // If there is no phonebook application:
                if (!resolveActivityFor(this, pickContactIntent)) return@apply
                setOnClickListener {
                    contactsActivityLauncher.launch(pickContactIntent, options)
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
                    Contacts.CONTENT_URI, null,
                    null, null, null, null
                ) ?: return@setOnClickListener

                while (contactsCursor.moveToNext()) {
                    val contactId = contactsCursor.getString(contactsCursor.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY))
                    if (contactId == crime.suspect) {
                        contactsCursor.use {
                            val phonesCursor = resolver.query(
                                Phone.CONTENT_URI, null,
                                "${Phone.DISPLAY_NAME_PRIMARY}='$contactId'",
                                null, null
                            )
                            phonesCursor?.use {
                                while (it.moveToNext()) {
                                    val numberStr = it.getString(it.getColumnIndex(Phone.NUMBER))
                                    Intent(Intent.ACTION_CALL).apply { // TODO: better use ACTION_DEAL instead
                                        data = Uri.parse("tel:$numberStr")
                                    }.also { intent ->
                                        startActivity(intent)
                                    }
                                    return@setOnClickListener
                                }
                            }
                        }
                    }
                }
            }

            crimeCameraButton.apply {
                val captureImageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // If there is no camera application:
                if (!resolveActivityFor(this, captureImageIntent)) return@apply

                setOnClickListener {
                    captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                    val cameraActivities = packageManager.queryIntentActivities(captureImageIntent,
                        PackageManager.MATCH_DEFAULT_ONLY)

                    for (cameraActivity in cameraActivities) {
                        requireActivity().grantUriPermission(
                            cameraActivity.activityInfo.packageName,
                            photoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    }
                    cameraActivityLauncher.launch(captureImageIntent)
                }
            }
        } // with

        setupDatePickerDialogFragmentListener()
        setupTimePickerDialogFragmentListener()
    }

    override fun onStop() {
        super.onStop()
        if (crime.title.isBlank() || crime.title.isEmpty())
            crimeViewModel.deleteCrime(crime)
        else {
            crimeViewModel.updateCrime(crime)
            showToast(R.string.crime_saved)
        }
        binding.progressBar.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    private fun updateDate(date: Date) {
        crime = crime.copy(date = date)
        updateUI()
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateUI() {
        with(binding) {
            crimeTitleTextView.setText(crime.title)

            val dateFormat = SimpleDateFormat("E, d MMMM, yyyy")
            val timeFormat = SimpleDateFormat("h:mm")
            val calendar = Calendar.getInstance().apply { time = crime.date }
            crimeDateButton.text = dateFormat.format(calendar.time)
            crimeTimeButton.text = timeFormat.format(calendar.time)

            crimeSolvedCheckbox.isChecked  = crime.isSolved
            crimeSeriousCheckbox.isChecked = crime.requiresPolice
            crimeSolvedCheckbox.jumpDrawablesToCurrentState() // cancelling checkbox animation
            crimeSeriousCheckbox.jumpDrawablesToCurrentState()

            if (crime.suspect.isNotEmpty()) {
                crimeChooseSuspectButton.text    = getString(R.string.crime_suspect_text, crime.suspect)
                crimeCallSuspectButton.text      = getString(R.string.crime_call_suspect_text)
                crimeCallSuspectButton.isEnabled = true
            } else {
                crimeChooseSuspectButton.text    = getString(R.string.crime_choose_suspect_text)
                crimeCallSuspectButton.text      = getString(R.string.crime_call_suspect_disabled_text)
                crimeCallSuspectButton.isEnabled = false
            }
        }
        updatePhotoView()
    }

    private fun updatePhotoView() {
        val bitmap = if (photoFile.exists()) {
            getScaledBitmap(photoFile.path, requireActivity())
        } else {
            null
        }
        binding.crimePhoto.setImageBitmap(bitmap)
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

    private fun resolveActivityFor(view: View, intent: Intent): Boolean {
        val packageManager = requireActivity().packageManager
        val resolvedActivity = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resolvedActivity == null) {
            view.isEnabled = false
            return false
        }
        return true
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