package com.app.androidcodingchellange.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.androidcodingchellange.databinding.SplashScreenBinding
import com.app.androidcodingchellange.utils.SPLASH_SCREEN_TIMER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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