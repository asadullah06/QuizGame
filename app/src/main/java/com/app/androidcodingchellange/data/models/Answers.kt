package com.app.androidcodingchellange.data.models

data class Answers(
    val option: String,
    val optionKey: String,
    val isOptionSelected: Boolean = false,
    val isCorrectOption: Boolean = false
)