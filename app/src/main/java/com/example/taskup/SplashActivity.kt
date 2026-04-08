package com.example.taskup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class SplashActivity : AppCompatActivity() {

    private lateinit var btnEnterApp: MaterialButton

    private val handler = Handler(Looper.getMainLooper())
    private val navigateRunnable = Runnable {
        openMain()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        btnEnterApp = findViewById(R.id.btnEnterApp)

        btnEnterApp.setOnClickListener {
            handler.removeCallbacks(navigateRunnable)
            openMain()
        }

        handler.postDelayed(navigateRunnable, 3000)
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(navigateRunnable)
    }
}