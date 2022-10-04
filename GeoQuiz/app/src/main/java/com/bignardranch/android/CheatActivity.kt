package com.bignardranch.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignardranch.android.geoquiz.answer_is_true" // Intent extra key

// TODO: remove from AndroidManifest and see what happens
class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private var answer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            answerTextView.setText(if (answer) R.string.true_button else R.string.false_button)
            setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_ANSWER_SHOWN, true))
        }

        // Activity.intent - intent that started this activity
        answer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
    }

//    private fun setAnswerShownResult(isAnswerShown: Boolean) {
//        setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_ANSWER_IS_SHOWN, isAnswerShown))
//    }

    companion object {
        // Intent to start this Activity with the given context and answer
        fun newIntent(packageContext: Context, answer: Boolean): Intent =
            Intent(packageContext, this::class.java).putExtra(EXTRA_ANSWER_IS_TRUE, answer)
    }
}