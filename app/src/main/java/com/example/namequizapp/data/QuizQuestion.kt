package com.example.namequizapp.data

/**
 * Object to hold data for one quiz question
 */
data class QuizQuestion(
    val image: String,
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val correct: QuizEntryModel
)
