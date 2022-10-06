package com.bignardranch.android

//import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

import com.bignardranch.android.geoquiz.CheckedQuestion
import com.bignardranch.android.geoquiz.GeoQuizViewModel


private const val TAG = "MainActivity"
const val DEBUG = false

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var prevButton: View // ImageButton in activity_main.xml and Button in land-activity_main.xml
    private lateinit var nextButton: View //

    private val viewModel: GeoQuizViewModel by viewModels() // TODO: see delegates

    // Must call before STARTED Activity state ("LifecycleOwners must call register before they are STARTED")
    private val cheatActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val isCheated = result.data?.getBooleanExtra(
                EXTRA_ANSWER_SHOWN, false
            ) ?: false
            if (isCheated) viewModel.setCurrentAnswerCheated()
        }
    }

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
            viewModel.correctAnswers = it.getInt("correctAnswers", 0)
            viewModel.checkedAnswers =
                it.getParcelableArrayList<CheckedQuestion>("checkedAnswers")?.toMutableList()
                    ?: mutableListOf()
            // TODO: Add all properties
        }

        questionTextView = findViewById(R.id.question_text_view)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        cheatButton.setOnClickListener {
            cheatActivityLauncher.launch(CheatActivity.newIntent(this, viewModel.currentQuestionAnswer))
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
        with(viewModel) { currentQuestion = ++currentQuestion % questionsSize }
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
        questionTextView.setText(viewModel.currentQuestionText)
        (!viewModel.isCurrentAnswerChecked).let {
            setAnswerButtonsEnabled(it)
            setCheatButtonEnabled(it)
        }
    }

    private fun checkAnswer(answer: Boolean) {
        setAnswerButtonsEnabled(false)
        setCheatButtonEnabled(false)

        with(viewModel) {
            val toastResId = when {
                viewModel.isCurrentAnswerCheated -> { ++correctAnswers; R.string.judgment_toast }
                answer == currentQuestionAnswer -> { ++correctAnswers; R.string.correct_toast }
                else -> R.string.incorrect_toast
            }

            setCurrentAnswerChecked()

            val toast = if (isAllAnswersChecked) {
                val isCorrectStr = if (answer == currentQuestionAnswer) "Correct!" else "Incorrect"
                Toast.makeText(
                    this@MainActivity,
                    String.format(
                        "$isCorrectStr Your have answered $correctAnswers of $questionsSize (%.2f%%).\n",
                        correctAnswers.toDouble() / questionsSize.toDouble() * 100.toDouble()
                    ),
                    Toast.LENGTH_LONG
                )
            } else {
                Toast.makeText(
                    this@MainActivity,
                    toastResId,
                    Toast.LENGTH_SHORT
                )
            }
            toast.show()
            if (isAnyAnswersCheated && isAllAnswersChecked) {
                Toast.makeText(this@MainActivity,
                    "Your have cheated $cheatedAnswers times",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setAnswerButtonsEnabled(enabled: Boolean) {
        falseButton.isEnabled = enabled
        trueButton.isEnabled = enabled
    }

    private fun setCheatButtonEnabled(enabled: Boolean) { cheatButton.isEnabled = enabled }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSaveInstanceState() called")
        savedInstanceState.putAll(
            bundleOf(                                   // TODO: use delegates for keys
                "currentQuestion" to viewModel.currentQuestion,
                "correctAnswers" to viewModel.correctAnswers,
                "checkedAnswers" to viewModel.checkedAnswers.toMutableList()
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