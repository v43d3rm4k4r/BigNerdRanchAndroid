package com.bignardranch.android

//import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

import com.bignardranch.android.geoquiz.GeoQuizViewModel
import java.lang.Exception


private const val TAG = "MainActivity"
const val DEBUG = true

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: View // ImageButton in activity_main.xml and Button in land-activity_main.xml
    private lateinit var nextButton: View //

    private val viewModel: GeoQuizViewModel by viewModels() // TODO: see delegates


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (DEBUG) {
            Toast.makeText(
                this,
                "onCreate() Toast",
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d(TAG, "onCreate() called")
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            viewModel.currentQuestion = it.getInt("currentQuestion", 0)
            viewModel.correctAnswers = it.getInt("currentAnswers", 0)
            viewModel.checkedAnswers = it.getBooleanArray("checkedAnswers")?.toMutableList() ?: mutableListOf()
        }

        questionTextView = findViewById(R.id.question_text_view)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        prevButton.setOnClickListener {
            showPrevQuestion()
        }

        nextButton.setOnClickListener {
            showNextQuestion()
        }

        questionTextView.setOnClickListener {
            showNextQuestion()
        }

        updateQuestion()
    }

    private fun showNextQuestion() {
        //with(viewModel) { currentQuestion = ++currentQuestion % questionsSize }
        updateQuestion()
    }

    private fun showPrevQuestion() {
        with(viewModel) {
            --currentQuestion
            if (currentQuestion < 0) currentQuestion = questionsSize - 1
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        Log.d(TAG, "Updating question text", Exception())
        questionTextView.setText(viewModel.currentQuestionText)
        if (viewModel.isCurrentAnswerChecked()) setAnswerButtonsEnabled(false)
        else setAnswerButtonsEnabled(true)
    }

    private fun checkAnswer(answer: Boolean) {
        setAnswerButtonsEnabled(false)

        with(viewModel) {
            val toastStr = if (answer == currentQuestionAnswer) {
                ++correctAnswers
                R.string.correct_toast
            } else R.string.incorrect_toast

            setCurrentCheckedAnswer()

            val toast = if (isAllAnswersChecked()) {
                Toast.makeText(
                    this@MainActivity,
                    String.format(
                        "Correct! Your have answered $correctAnswers of $questionsSize (%.2f%%)",
                        correctAnswers.toDouble() / questionsSize.toDouble() * 100.toDouble()
                    ),
                    Toast.LENGTH_LONG
                )
            } else {
                Toast.makeText(
                    this@MainActivity,
                    toastStr,
                    Toast.LENGTH_SHORT
                )
            }
//            toast.setGravity(
//                if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) Gravity.TOP else Gravity.BOTTOM,
//                0, 0
//            )
            toast.show()
        }
    }

    private fun setAnswerButtonsEnabled(enabled: Boolean) {
        falseButton.isEnabled = enabled
        trueButton.isEnabled = enabled
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSaveInstanceState() called")
        savedInstanceState.putAll(
            bundleOf(                                   // TODO: use delegates for keys
                "currentQuestion" to viewModel.currentQuestion,
                "correctAnswers" to viewModel.correctAnswers,
                "checkedAnswers" to viewModel.checkedAnswers.toBooleanArray()
            )
        )
    }

    override fun onStart() {
        super.onStart()
        if (DEBUG) {
            Toast.makeText(
                this,
                "onStart() Toast",
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        if (DEBUG) {
            Toast.makeText(
                this,
                "onResume() Toast",
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        if (DEBUG) {
            Toast.makeText(
                this,
                "onPause() Toast, isFinishing == $isFinishing",
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d(TAG, "onPause() called, isFinishing == $isFinishing")
    }

    override fun onStop() {
        super.onStop()
        if (DEBUG) {
            Toast.makeText(
                this,
                "onStop() Toast",
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (DEBUG) {
            Toast.makeText(
                this,
                "onDestroy() Toast",
                Toast.LENGTH_SHORT
            ).show()
        }
        Log.d(TAG, "onDestroy() called")
    }
}