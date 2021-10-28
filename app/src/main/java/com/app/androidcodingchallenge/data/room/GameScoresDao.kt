package com.app.androidcodingchallenge.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface GameScoresDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(gameScoresEntity: GameScoresEntity): Long

    @Query("SELECT * FROM game_scores")
    suspend fun getAllScores(): List<GameScoresEntity>
}