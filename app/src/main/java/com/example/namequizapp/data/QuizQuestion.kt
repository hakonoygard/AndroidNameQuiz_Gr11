package com.example.namequizapp.data

data class QuizQuestion(
    val image: String,
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val correct: QuizEntryModel
)
