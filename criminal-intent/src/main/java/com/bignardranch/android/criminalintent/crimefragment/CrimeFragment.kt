package com.bignardranch.android.criminalintent.crimefragment

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bignardranch.android.criminalintent.databinding.FragmentCrimeBinding
import com.bignardranch.android.criminalintent.model.Crime
import java.util.*

private const val ARG_CRIME_ID = "crime_id"

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
        binding.crimeDateButton.apply {
            text = DateFormat.format("dd-MM-yyyy hh:mm:ss a", crime.date) // TODO: should be like "Monday, Jul 22, 2019"
            isEnabled = false
        }
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
    }

    override fun onStart() {
        super.onStart()

//        val titleWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                crime.title = s.toString()
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//        }
//        titleField.addTextChangedListener(titleWatcher)
        binding.crimeTitleTextView.doOnTextChanged { text, _, _, _ ->  crime.title = text.toString() }
        binding.crimeSolvedCheckbox.setOnCheckedChangeListener { _, isChecked -> crime.isSolved = isChecked }
    }

    override fun onStop() {
        super.onStop()
        crimeViewModel.saveCrime(crime)
    }

    private fun updateUI() {
        with(binding) {
            crimeTitleTextView.setText(crime.title)
            crimeDateButton.text = crime.date.toString()
            crimeSolvedCheckbox.isChecked = crime.isSolved
            crimeSolvedCheckbox.jumpDrawablesToCurrentState() // cancelling checkbox animation
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