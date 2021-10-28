package com.app.androidcodingchallenge.repositories

import com.app.androidcodingchallenge.data.room.GameScoresDao
import com.app.androidcodingchallenge.data.room.GameScoresEntity
import com.app.androidcodingchallenge.utils.Resource
import java.lang.Exception

class DefaultGameScoreRepository(
    private val gameScoresDao: GameScoresDao
) : GameScoresRepository {

    override suspend fun getAllGameScores(): Resource<List<GameScoresEntity>> {
        return try {
            val response = gameScoresDao.getAllScores()
            if (response.isNotEmpty()) {
                Resource.Success(response)
            } else
                Resource.Error("No record found")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    override suspend fun addGameScore(gameScoresEntity: GameScoresEntity): Long {
        return gameScoresDao.insertScore(gameScoresEntity)
    }
}