package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

private const val TAG = "CheatActivity"

private const val KEY_ANSWER_SHOWN = "answerIsShown"

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    var answerIsTrue = false

    private lateinit var showAnswerButton: Button
    private lateinit var answerTextView: TextView

    private val cheatViewModel: CheatViewModel by lazy {
        ViewModelProviders.of(this).get(CheatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        val answerIsShown = savedInstanceState?.getBoolean(KEY_ANSWER_SHOWN, false) ?: false
        cheatViewModel.answerIsShown = answerIsShown

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        showAnswerButton = findViewById(R.id.show_answer_button)
        answerTextView = findViewById(R.id.answer_text_view)

        showAnswerButton.setOnClickListener {
            cheatViewModel.answerIsShown = true
            getAnswerText()
        }

        if(cheatViewModel.answerIsShown){
            getAnswerText()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, cheatViewModel.answerIsShown)
    }

    /**
     * Get text of answer based on answer being true or false
     */
    private fun getAnswerText(){
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
        setAnswerShownResult()
    }

    /**
     * Set intent for activity manager setting answer has shown (cheated)
     */
    private fun setAnswerShownResult(){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.answerIsShown)
        }
        Log.i(TAG, "cheated")
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        /**
         * Create new intent indicating if answer is true
         */
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}