package com.app.androidcodingchallenge.ui

import androidx.lifecycle.ViewModel
import com.app.androidcodingchallenge.repositories.GameScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameDashboardViewModel @Inject constructor(
    private val gameScoresRepository: GameScoresRepository
) : ViewModel() {
}