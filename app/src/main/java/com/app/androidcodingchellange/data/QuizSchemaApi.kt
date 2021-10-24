package com.app.androidcodingchellange.data

import com.app.androidcodingchellange.data.models.QuizSchemaResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuizSchemaApi {

    @GET("/3acef828-7f8f-4905-a12e-1b057db45f48")
    suspend fun getQuizSchema(): Response<QuizSchemaResponse>
}