package com.bignerdranch.android.geoquiz.cheatactivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.R

const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true" // Intent extra key

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView
    private var answer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text_view)

        showAnswerButton.setOnClickListener {
            answerTextView.setText(if (answer) R.string.true_button else R.string.false_button)
            setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_ANSWER_SHOWN, true))
        }

        updateAPILevelTextView()

        // Activity.intent - intent that started this activity
        answer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
    }

    @SuppressLint("SetTextI18n")
    fun updateAPILevelTextView() {
        apiLevelTextView.text = resources.getString(R.string.api_level) + " ${Build.VERSION.SDK_INT}"
    }

    companion object {
        // Intent to start this Activity with the given context and answer
        fun newIntent(packageContext: Context, answer: Boolean): Intent =
            Intent(packageContext, CheatActivity::class.java).putExtra(EXTRA_ANSWER_IS_TRUE, answer)
    }
}