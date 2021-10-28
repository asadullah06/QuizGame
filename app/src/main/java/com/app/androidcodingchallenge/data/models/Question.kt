package com.app.androidcodingchallenge.data.models

data class Question(
    val answers: Map<String, String>,
    val correctAnswer: String,
    val question: String,
    var questionImageUrl: String?,
    val score: Int,
    val type: String
)