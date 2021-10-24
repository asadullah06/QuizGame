package com.app.androidcodingchellange.repositories

import com.app.androidcodingchellange.data.QuizSchemaApi
import com.app.androidcodingchellange.data.models.QuizSchemaResponse
import com.app.androidcodingchellange.utils.Resource
import java.lang.Exception
import javax.inject.Inject


class DefaultMainQuizRepository @Inject constructor(
    private val api: QuizSchemaApi
) : MainQuizRepository {
    override suspend fun getQuestions(): Resource<QuizSchemaResponse> {
        return try {
            val response = api.getQuizSchema()
            val result = response.body()

            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }


}