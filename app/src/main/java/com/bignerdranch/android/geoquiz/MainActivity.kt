package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

/**
 *  Class that represents the Main view of the app, where the questions are displayed
 *
 * @author Federico Rusconi
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button

    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    /** Receive data from child activity */
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            Log.i(TAG, "cheater received")
            val hasCheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.setQuestionCheated(hasCheated)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get index of current selected question
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        //Set elements
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById((R.id.question_text_view))
        //Set listeners
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            //Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            resultLauncher.launch(intent)
            /* Deprecated
            startActivityForResult(intent, REQUEST_CODE_CHEAT)*/
        }

        updateQuestion()
    }

    /* Deprecated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode !== Activity.RESULT_OK){
            return
        }

        if(requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }*/

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    /** Update current question displayed */
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        enableDisableBtns()
    }

    /**
     * Check if answer provided is correct
     * @param userAnswer User's answer (true/false)
     */
    private fun checkAnswer(userAnswer: Boolean) {
        //Find correct answer
        val correctAnswer = quizViewModel.currentQuestionAnswer
        //Set question as answered
        quizViewModel.setQuestionAnswered(true)
        quizViewModel.questionsGiven ++
        //Disabled buttons
        enableDisableBtns()
        //Get string depending on cheated/correct/incorrect answer
        val messageResId = when {
            quizViewModel.isCurrentQuestionCheated -> R.string.judgment_toast
            userAnswer == correctAnswer -> {
                quizViewModel.score++
                R.string.correct_toast
            }
            else -> R.string.incorrect_toast
        }
        //Display Toast
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        //If user answered all questions
        if(quizViewModel.questionsGiven == quizViewModel.questionBankSize){
            //Display message with score
            val percentage = quizViewModel.score * 100 / quizViewModel.questionsGiven
            val toastString = getString(R.string.score_toast, percentage)
            Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Enable or Disable true/false buttons
     */
    private fun enableDisableBtns(){
        trueButton.isEnabled = !quizViewModel.isCurrentQuestionAnswered
        falseButton.isEnabled = !quizViewModel.isCurrentQuestionAnswered
    }
}