package com.bignardranch.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels // ??
import com.bignardranch.android.geoquiz.GeoQuizViewModel
import com.bignardranch.android.geoquiz.Question


private const val TAG = "MainActivity"
private const val DEBUG = false

/**
 * @see android.widget.Toast.setGravity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: View // ImageButton in activity_main.xml and Button in land-activity_main.xml
    private lateinit var nextButton: View //
    //private val model: GeoQuizViewModel by viewModels() // NOT WORKING

    private val questions = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private val checkedAnswers = MutableList(6) { false }

    private var currentQuestion = 0
    private var correctAnswers = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val model: GeoQuizViewModel by activityViewModels() // NOT WORKING

        if (DEBUG) {
            Toast.makeText(
                this,
                "onCreate() Toast",
                Toast.LENGTH_SHORT
            ).show()
        }

        setContentView(R.layout.activity_main)

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

    override fun onStart() {
        super.onStart()
        if (DEBUG) {
            Toast.makeText(
                this,
                "onStart() Toast",
                Toast.LENGTH_SHORT
            ).show()
        }
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
    }

    override fun onPause() {
        super.onPause()
        if (DEBUG) {
            Toast.makeText(
                this,
                "onPause() Toast",
                Toast.LENGTH_SHORT
            ).show()
        }
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
    }

    private fun showNextQuestion() {
        currentQuestion = ++currentQuestion % questions.size
        updateQuestion()
    }

    private fun showPrevQuestion() {
        --currentQuestion
        if (currentQuestion < 0) currentQuestion = questions.size - 1
        updateQuestion()
    }

    private fun updateQuestion() {
        questionTextView.setText(questions[currentQuestion].textResId)
        if (checkedAnswers[currentQuestion]) setAnswerButtonsEnabled(false)
        else setAnswerButtonsEnabled(true)
    }

    private fun checkAnswer(answer: Boolean) {
        setAnswerButtonsEnabled(false)

        val toastStr = if (answer == questions[currentQuestion].answer) {
            ++correctAnswers
            R.string.correct_toast
        } else R.string.incorrect_toast

        checkedAnswers[currentQuestion] = true

        val toast = if (checkedAnswers.all { it }) {
            Toast.makeText(
                this,
                String.format("Correct! Your have answered $correctAnswers of ${questions.size} (%.2f%%)", correctAnswers.toDouble() / questions.size.toDouble() * 100.toDouble()),
                Toast.LENGTH_LONG
            )
        } else {
            Toast.makeText(
                this,
                toastStr,
                Toast.LENGTH_SHORT
            )
        }.show()
    }

    private fun setAnswerButtonsEnabled(enabled: Boolean) {
        falseButton.isEnabled = enabled
        trueButton.isEnabled = enabled
    }
}