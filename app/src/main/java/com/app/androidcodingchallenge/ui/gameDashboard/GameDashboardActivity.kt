package com.app.androidcodingchallenge.ui.gameDashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.androidcodingchallenge.BaseActivity
import com.app.androidcodingchallenge.R
import com.app.androidcodingchallenge.databinding.HomeScreenActivityBinding
import com.app.androidcodingchallenge.ui.gameMainQuiz.MainQuizActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/**
 * Main Menu Screen will show the possible features of the screen i.e
 * start new game and see highest from all quizzes.
 */

@AndroidEntryPoint
class GameDashboardActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: HomeScreenActivityBinding

    val viewModel: GameDashboardViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()

        collectGameHighestScore()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGameHighestScore()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_start_game -> {
                startNewGame()
            }
        }
    }

    /**
     * Method will set click listeners on all required views on this screen
     */
    private fun setClickListeners() {
        binding.btnStartGame.setOnClickListener(this)
    }

    /**
     * Method will start game screen
     */
    private fun startNewGame() {
        val intent = Intent(this, MainQuizActivity::class.java)
        startActivity(intent)
    }

    private fun collectGameHighestScore() {
        lifecycleScope.launchWhenStarted {
            viewModel.gameHScoreStateFlow.collect { events ->
                when (events) {
                    is GameDashboardViewModel.GameScoresEvents.Success -> {
                        binding.textViewHighestScore.text = "${events.highestScore}"
                    }
                    is GameDashboardViewModel.GameScoresEvents.Error -> {
                        Snackbar.make(binding.root, events.message, Snackbar.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }
    }

}