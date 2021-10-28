package com.app.androidcodingchallenge.repositories

import com.app.androidcodingchallenge.data.models.QuizSchemaResponse
import com.app.androidcodingchallenge.utils.Resource

interface MainQuizRepository {

    suspend fun getQuestions():Resource<QuizSchemaResponse>
}