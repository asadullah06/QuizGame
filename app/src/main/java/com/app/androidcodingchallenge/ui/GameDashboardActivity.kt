package com.app.androidcodingchallenge.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.androidcodingchallenge.BaseActivity
import com.app.androidcodingchallenge.R
import com.app.androidcodingchallenge.databinding.HomeScreenActivityBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Menu Screen will show the possible features of the screen i.e
 * start new game and see highest from all quizzes.
 */

@AndroidEntryPoint
class GameDashboardActivity : BaseActivity(), View.OnClickListener {
    lateinit var binding: HomeScreenActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClickListeners()
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

}