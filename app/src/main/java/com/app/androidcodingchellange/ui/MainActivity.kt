package com.app.androidcodingchellange.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.androidcodingchellange.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivityBinding

    @Inject
    lateinit var testString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = testString
    }
}