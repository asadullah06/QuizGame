package com.app.androidcodingchallenge.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.androidcodingchallenge.databinding.SplashScreenBinding
import com.app.androidcodingchallenge.utils.SPLASH_SCREEN_TIMER
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    lateinit var binding: SplashScreenBinding

    @Inject
    lateinit var testString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.text = testString

        Thread {
            Thread.sleep(SPLASH_SCREEN_TIMER)
            this.runOnUiThread {
                this.finish()
                startGameDashboardActivity()
            }
        }.start()
    }

    private fun startGameDashboardActivity() {
        val intent = Intent(this, GameDashboardActivity::class.java)
        startActivity(intent)
    }
}