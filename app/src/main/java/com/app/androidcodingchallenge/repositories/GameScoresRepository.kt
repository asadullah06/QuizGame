package com.app.androidcodingchallenge.repositories

import com.app.androidcodingchallenge.data.room.GameScoresEntity
import com.app.androidcodingchallenge.utils.Resource

interface GameScoresRepository {

    suspend fun getAllGameScores(): Resource<List<GameScoresEntity>>
    suspend fun addGameScore(gameScoresEntity: GameScoresEntity): Long

}