package com.app.androidcodingchellange.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.androidcodingchellange.data.models.Question
import com.app.androidcodingchellange.data.models.QuizSchemaResponse
import com.app.androidcodingchellange.repositories.MainQuizRepository
import com.app.androidcodingchellange.utils.DispatcherProvider
import com.app.androidcodingchellange.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainQuizViewModel @Inject constructor(
    private val repository: MainQuizRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    lateinit var response: QuizSchemaResponse
    var questionToPopulateIndex = 0

    sealed class QuizSchemaEvents {
        class Success(
            val questionToPopulateIndex: Int,
            val totalQuestions: Int,
            val questionObject: Question
        ) : QuizSchemaEvents()

        class Failure(val errorText: String) : QuizSchemaEvents()
        object Loading : QuizSchemaEvents()
        object Empty : QuizSchemaEvents()
    }

    private val _quizSchema = MutableStateFlow<QuizSchemaEvents>(QuizSchemaEvents.Empty)
    val quizSchema: StateFlow<QuizSchemaEvents> = _quizSchema

    fun getQuestions() {

        viewModelScope.launch(dispatcher.io) {
            _quizSchema.value = QuizSchemaEvents.Loading

            when (val quizSchemaResponse = repository.getQuestions()) {
                is Resource.Error -> _quizSchema.value =
                    QuizSchemaEvents.Failure(quizSchemaResponse.message!!)
                is Resource.Success -> {
                    response = quizSchemaResponse.data!!
                    _quizSchema.value = QuizSchemaEvents.Success(
                        questionToPopulateIndex,
                        response.questions.size,
                        response.questions[questionToPopulateIndex]
                    )
                }
            }
        }
    }

    fun loadNextQuestion() {
        viewModelScope.launch(dispatcher.main) {
            questionToPopulateIndex++
            _quizSchema.value = QuizSchemaEvents.Success(
                questionToPopulateIndex,
                response.questions.size,
                response.questions[questionToPopulateIndex]
            )
        }
    }
}