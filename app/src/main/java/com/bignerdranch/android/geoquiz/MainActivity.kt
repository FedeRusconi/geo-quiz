package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private var questionsGiven: Int = 0
    private var score: Int = 0

    private var currentIndex = 0
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById((R.id.question_text_view))

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        prevButton.setOnClickListener {
            currentIndex = if(currentIndex == 0) questionBank.size - 1 else currentIndex - 1
            updateQuestion()
        }

        updateQuestion()
    }

    /** Update current question */
    private fun updateQuestion() {
        val currentQuestion = questionBank[currentIndex]
        val questionTextResId = currentQuestion.textResId
        questionTextView.setText(questionTextResId)
        enableDisableBtns(currentQuestion)
    }

    /**
     * Check if answer provided is correct
     * @param userAnswer User's answer (true/false)
     */
    private fun checkAnswer(userAnswer: Boolean) {
        //Find current question
        val currentQuestion = questionBank[currentIndex]
        //Find correct answer
        val correctAnswer = currentQuestion.answer
        //Set question as answered
        currentQuestion.answered = true
        questionsGiven ++
        //Disabled buttons
        enableDisableBtns(currentQuestion)
        //Get string depending on correct/incorrect answer
        val messageResId = if (userAnswer == correctAnswer) {
            score++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        //Display Toast
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        //If user answered all questions
        if(questionsGiven == questionBank.size){
            //Display message with score
            val percentage = score * 100 / questionsGiven
            val toastString = getString(R.string.score_toast, percentage)
            Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Enable or Disable true/false buttons
     * @param currentQuestion The current question displayed
     */
    private fun enableDisableBtns(currentQuestion: Question){
        trueButton.isEnabled = !currentQuestion.answered
        falseButton.isEnabled = !currentQuestion.answered
    }
}