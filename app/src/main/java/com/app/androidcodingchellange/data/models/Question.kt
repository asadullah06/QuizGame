package com.app.androidcodingchellange.data.models

import com.google.gson.JsonObject

data class Question(
    val answers: Map<String, String>,
    val correctAnswer: String,
    val question: String,
    val questionImageUrl: String?,
    val score: Int,
    val type: String
)