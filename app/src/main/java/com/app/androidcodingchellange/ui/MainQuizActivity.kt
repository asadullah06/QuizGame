package com.app.androidcodingchellange.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.app.androidcodingchellange.BaseActivity
import com.app.androidcodingchellange.databinding.MainQuizActivityBinding
import kotlinx.coroutines.flow.collect

class MainQuizActivity : BaseActivity() {
    private val TAG = "MainQuizActivity"
    private lateinit var binding: MainQuizActivityBinding

    private val viewModel: MainQuizViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainQuizActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getQuestions()

        lifecycleScope.launchWhenStarted {
            viewModel.quizSchema.collect { events ->
                when (events) {
                    is MainQuizViewModel.QuizSchemaEvents.Success -> {
                        binding.pbLoading.isVisible = false
                        Log.i(TAG, events.result.toString())
                    }
                    is MainQuizViewModel.QuizSchemaEvents.Failure -> {
                        binding.pbLoading.isVisible = false
                        Log.i(TAG, events.errorText)
                    }
                    is MainQuizViewModel.QuizSchemaEvents.Loading -> {
                        binding.pbLoading.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }


}