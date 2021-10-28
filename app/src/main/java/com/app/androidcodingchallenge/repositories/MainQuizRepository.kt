package com.app.androidcodingchellange.repositories

import com.app.androidcodingchellange.data.models.QuizSchemaResponse
import com.app.androidcodingchellange.utils.Resource

interface MainQuizRepository {

    suspend fun getQuestions():Resource<QuizSchemaResponse>
}