package com.bignardranch.android.criminalintent

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bignardranch.android.criminalintent.model.Crime

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        titleField = view.findViewById(R.id.crime_title_text_view)
        dateButton = view.findViewById(R.id.crime_date_button)
        dateButton.apply {
            text = DateFormat.format("dd-MM-yyyy hh:mm:ss a", crime.date) // TODO: should be like "Monday, Jul 22, 2019"
            isEnabled = false
        }
        solvedCheckBox = view.findViewById(R.id.crime_solved_checkbox)
        return view
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
        titleField.doOnTextChanged { text, _, _, _ ->  crime.title = text.toString() }
        solvedCheckBox.setOnCheckedChangeListener { _, isChecked -> crime.isSolved = isChecked }
    }
}