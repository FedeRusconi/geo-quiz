package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel: ViewModel() {

    var questionsGiven = 0
    var score = 0
    var currentIndex = 0

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val isCurrentQuestionAnswered: Boolean
        get() = questionBank[currentIndex].answered

    val isCurrentQuestionCheated: Boolean
        get() = questionBank[currentIndex].cheated

    val questionBankSize: Int
        get() = questionBank.size

    /**
     * Set current question as answered/unanswered
     * @param value True/false
     */
    fun setQuestionAnswered(value:Boolean) {
        questionBank[currentIndex].answered = value
    }

    /**
     * Set current question as cheated/not cheated
     * @param value True/false
     */
    fun setQuestionCheated(value:Boolean) {
        questionBank[currentIndex].cheated = value
    }

    /**
     * Move to next question
     */
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    /**
     * Move to previous question
     */
    fun moveToPrev() {
        currentIndex = if(currentIndex == 0) questionBank.size - 1 else currentIndex - 1
    }

}