package com.bignardranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bignardranch.android.R

private const val TAG = "GeoQuizViewModel"

class GeoQuizViewModel : ViewModel() {
    init {
        Log.d(TAG, "ViewModel instance created")
    }

    private val questions = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    val questionsSize: Int
        get() = questions.size

    var checkedAnswers = MutableList(questions.size) { false }
    fun setCurrentCheckedAnswer() {
        checkedAnswers[currentQuestion] = true
    }

    fun isCurrentAnswerChecked() = checkedAnswers[currentQuestion]
    fun isAllAnswersChecked() = checkedAnswers.all { it }

    var currentQuestion = 0
    var correctAnswers = 0
    var isCheater = false

    val currentQuestionAnswer: Boolean
        get() = questions[currentQuestion].answer

    val currentQuestionText: Int
        get() = questions[currentQuestion].textResId

    override fun onCleared() {
        Log.d(TAG, "$TAG.onCleared() called")
    }
}