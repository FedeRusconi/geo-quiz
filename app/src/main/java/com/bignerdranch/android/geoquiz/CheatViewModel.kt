package com.bignerdranch.android.geoquiz

import androidx.lifecycle.ViewModel

/**
 *  Class that represents the ViewModel for the Cheat activity
 *  This class is used maintain the activity's state when configurations change (e.g. rotate)
 *
 * @author Federico Rusconi
 *
 */
class CheatViewModel: ViewModel() {

    var answerIsShown = false

}