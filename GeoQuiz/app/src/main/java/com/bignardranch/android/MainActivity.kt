package com.bignardranch.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.bignardranch.android.geoquiz.Question

private const val TAG = "MainActivity"

/**
 * @see android.widget.Toast.setGravity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton

    private val questions = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(
            this,
            "onCreate() Toast",
            Toast.LENGTH_SHORT
        ).show()

        Log.d(TAG, "onCreate() called")
        super.setContentView(R.layout.activity_main)

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
        Toast.makeText(
            this,
            "onStart() Toast",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(
            this,
            "onResume() Toast",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(
            this,
            "onPause() Toast",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(
            this,
            "onStop() Toast",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(
            this,
            "onDestroy() Toast",
            Toast.LENGTH_SHORT
        ).show()
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
    }

    private fun checkAnswer(answer: Boolean) {
        Toast.makeText(
            this,
            if (answer == questions[currentQuestion].answer) R.string.correct_toast
            else R.string.incorrect_toast,
            Toast.LENGTH_SHORT
        ).show()
    }
}