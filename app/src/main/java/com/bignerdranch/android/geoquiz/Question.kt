package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes

/**
 *  Class that represents the Question object
 *
 * @author Federico Rusconi
 *
 */
data class Question(@StringRes val textResId: Int,
                    val answer: Boolean,
                    var answered: Boolean = false,
                    var cheated: Boolean = false)