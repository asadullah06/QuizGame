package com.app.androidcodingchallenge.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameScoresEntity::class], version = 1,exportSchema = false)
abstract class GameScoresDatabase : RoomDatabase() {

    abstract fun gameScoresDao(): GameScoresDao

    companion object {
        val DATABASE_NAME = "game_scores_db"
    }
}