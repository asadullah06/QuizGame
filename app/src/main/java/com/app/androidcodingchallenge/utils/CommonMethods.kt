package com.app.androidcodingchallenge.utils

import com.app.androidcodingchallenge.data.room.GameScoresEntity
import java.util.concurrent.TimeUnit

object CommonMethods {
    /**
     *
     */
    fun formatToDigitalClock(milliSeconds: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds).toInt() % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds).toInt() % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds).toInt() % 60
        return when {
            hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
            minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
            seconds > 0 -> String.format("00:%02d", seconds)
            else -> {
                "00:00"
            }
        }
    }

    fun getHighestValueInList(list: List<GameScoresEntity>): Long {
        list.sortedByDescending { it.score }
        return list[list.size - 1].score
    }
}