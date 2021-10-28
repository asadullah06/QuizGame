package com.app.androidcodingchallenge.ui.gameMainQuiz

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.androidcodingchallenge.data.models.Question
import com.app.androidcodingchallenge.data.models.QuizSchemaResponse
import com.app.androidcodingchallenge.data.room.GameScoresEntity
import com.app.androidcodingchallenge.repositories.GameScoresRepository
import com.app.androidcodingchallenge.repositories.MainQuizRepository
import com.app.androidcodingchallenge.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainQuizViewModel @Inject constructor(
    private val mainQuizRepository: MainQuizRepository,
    private val gameScoresRepository: GameScoresRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    init {
        getGameHighestScore()
    }

    val TAG = "MainQuizViewModel"
    lateinit var response: QuizSchemaResponse
    var questionToPopulateIndex = 0
    lateinit var questionCountDown: CountDownTimer
    lateinit var answerCountDown: CountDownTimer

    sealed class QuizSchemaEvents {
        class Success(
            val questionToPopulateIndex: Int,
            val totalQuestions: Int,
            val questionObject: Question
        ) : QuizSchemaEvents()

        class Failure(val errorText: String) : QuizSchemaEvents()
        object Loading : QuizSchemaEvents()
        class CheckIsAnswerCorrect(val questionObject: Question) : QuizSchemaEvents()
        object Empty : QuizSchemaEvents()
    }

    sealed class TimerEvents {
        class OnStarted(val totalTime: Long) : TimerEvents()
        class OnTick(val long: Long) : TimerEvents()
        object OnFinished : TimerEvents()
        object Empty : TimerEvents()
    }

    private var gameHighestScore: Long = 0L
    private var currentGameScore: Long = 0L
    val questionTimerStateFlow = MutableStateFlow<TimerEvents>(TimerEvents.Empty)
    val answerTimerStateFlow = MutableStateFlow<TimerEvents>(TimerEvents.Empty)

    private val _quizSchema = MutableStateFlow<QuizSchemaEvents>(QuizSchemaEvents.Empty)
    val quizSchema: StateFlow<QuizSchemaEvents> = _quizSchema

    private fun getGameHighestScore() {
        viewModelScope.launch(dispatcher.io) {
            when (val response = gameScoresRepository.getAllGameScores()) {
                is Resource.Success -> {
                    gameHighestScore = CommonMethods.getHighestValueInList(response.data!!)
                }
                is Resource.Error -> {
                    Log.e(TAG, response.message!!)
                }
            }
        }
    }

    private fun addGameScore() {
        if (currentGameScore > gameHighestScore) {
            viewModelScope.launch(dispatcher.main) {
                gameScoresRepository.addGameScore(GameScoresEntity(currentGameScore))
            }
        }
    }

    fun getQuestions() {
        viewModelScope.launch(dispatcher.io) {
            _quizSchema.value = QuizSchemaEvents.Loading

            when (val quizSchemaResponse = mainQuizRepository.getQuestions()) {
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
            if (questionToPopulateIndex < response.questions.size) {
                _quizSchema.value = QuizSchemaEvents.Success(
                    questionToPopulateIndex,
                    response.questions.size,
                    response.questions[questionToPopulateIndex]
                )
            }
        }
    }

    fun startQuestionCountDown() {
        viewModelScope.launch(dispatcher.main) {
            questionTimerStateFlow.value = TimerEvents.OnStarted(TOTAL_QUESTION_COUNT_DOWN)
        }
        questionCountDown = object : CountDownTimer(TOTAL_QUESTION_COUNT_DOWN, 1000) {
            override fun onTick(p0: Long) {
                viewModelScope.launch(dispatcher.main) {
                    questionTimerStateFlow.value = TimerEvents.OnTick(p0)
                }
            }

            override fun onFinish() {
                viewModelScope.launch(dispatcher.main) {
                    questionTimerStateFlow.value = TimerEvents.OnFinished
                }
            }
        }.start()
    }


    fun checkIsAnswerCorrect() {
        viewModelScope.launch {
            _quizSchema.value =
                QuizSchemaEvents.CheckIsAnswerCorrect(response.questions[questionToPopulateIndex])
        }
    }

    /**
     * below method will stop the Question countDown timer if initialized.
     */
    fun stopQuestionCountDown() {
        if (::questionCountDown.isInitialized)
            questionCountDown.cancel()
    }

    fun startAnswerCountDown() {
        viewModelScope.launch(dispatcher.main) {
            answerTimerStateFlow.value = TimerEvents.OnStarted(TOTAL_ANSWER_COUNT_DOWN)
        }
        answerCountDown = object : CountDownTimer(TOTAL_ANSWER_COUNT_DOWN, 1000) {
            override fun onTick(p0: Long) {
                viewModelScope.launch(dispatcher.main) {
                    answerTimerStateFlow.value = TimerEvents.OnTick(p0)
                }
            }

            override fun onFinish() {
                viewModelScope.launch(dispatcher.main) {
                    answerTimerStateFlow.value = TimerEvents.OnFinished
                }
            }
        }.start()
    }

    /**
     * below method will stop the Answer countDown timer if initialized.
     */
    fun stopAnswerCountDown() {
        if (::answerCountDown.isInitialized)
            answerCountDown.cancel()
    }

    /**
     * below method will update the score of so far answered questions.
     * and check the game highest score as well accordingly.
     */
    fun updateCurrentGameScore(highestScore: Int) {
        currentGameScore += highestScore
        addGameScore()
    }
}