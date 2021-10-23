package com.app.androidcodingchellange.ui

import android.os.Bundle
import com.app.androidcodingchellange.BaseActivity
import com.app.androidcodingchellange.databinding.MainQuizActivityBinding

class MainQuizActivity : BaseActivity() {
    lateinit var binding: MainQuizActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainQuizActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}