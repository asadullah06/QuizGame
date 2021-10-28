package com.app.androidcodingchallenge.ui.gameDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.androidcodingchallenge.repositories.GameScoresRepository
import com.app.androidcodingchallenge.utils.CommonMethods
import com.app.androidcodingchallenge.utils.DispatcherProvider
import com.app.androidcodingchallenge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDashboardViewModel @Inject constructor(
    private val gameScoresRepository: GameScoresRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {
    private var _gameHScoreStateFlow = MutableStateFlow<GameScoresEvents>(GameScoresEvents.Empty)
    val gameHScoreStateFlow = _gameHScoreStateFlow

    sealed class GameScoresEvents {
        data class Success(val highestScore: Long) : GameScoresEvents()
        data class Error(val message: String) : GameScoresEvents()
        object Empty : GameScoresEvents()
    }

    fun getGameHighestScore() {
        viewModelScope.launch(dispatcher.io) {
            val response = gameScoresRepository.getAllGameScores()
            when (response) {
                is Resource.Success -> {
                    _gameHScoreStateFlow.value =
                        GameScoresEvents.Success(CommonMethods.getHighestValueInList(response.data!!))
                }
                is Resource.Error -> {
                    _gameHScoreStateFlow.value = GameScoresEvents.Error(response.message!!)
                }
            }
        }
    }


}