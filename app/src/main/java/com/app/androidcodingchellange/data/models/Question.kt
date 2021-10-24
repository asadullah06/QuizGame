package com.app.androidcodingchellange.data.models

data class Question(
    val answers: Answers,
    val correctAnswer: String,
    val question: String,
    val questionImageUrl: Any,
    val score: Int,
    val type: String
)