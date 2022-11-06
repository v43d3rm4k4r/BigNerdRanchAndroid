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
    val questionsSize: Int get() = questions.size

    var checkedAnswers = MutableList(questions.size) { CheckedQuestion( isChecked = false,
                                                                        isCheated = false) }

    fun setCurrentAnswerChecked() { checkedAnswers[currentQuestion].isChecked = true }
    fun setCurrentAnswerCheated() { checkedAnswers[currentQuestion].isCheated = true; ++cheatedAnswers }

    var currentQuestion = 0
    var correctAnswers = 0
    var cheatedAnswers = 0

    val isCurrentAnswerCheated: Boolean get() = checkedAnswers[currentQuestion].isCheated
    val isCurrentAnswerChecked: Boolean get() = checkedAnswers[currentQuestion].isChecked
    val isAllAnswersChecked: Boolean get() = checkedAnswers.all { it.isChecked }
    val isAnyAnswersCheated: Boolean get() = checkedAnswers.any { it.isCheated }
    val currentQuestionAnswer: Boolean get() = questions[currentQuestion].answer
    val currentQuestionText: Int get() = questions[currentQuestion].textResId
    val cheatsRemaining: Int get() = CHEATED_ANSWERS_LIMIT - cheatedAnswers
    val isCheatedLimitReached: Boolean get() = cheatsRemaining == 0

    override fun onCleared() {
        Log.d(TAG, "$TAG.onCleared() called")
    }

    companion object {
        const val CHEATED_ANSWERS_LIMIT = 3
    }
}