package com.app.androidcodingchellange.data.models

data class Answers(
    val option: String,
    val optionKey: String,
    var isOptionSelected: Boolean = false,
    var isCorrectOption: Boolean = false
)