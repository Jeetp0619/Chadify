package com.zybooks.chadify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.core.os.postDelayed

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Remove the status bar for a fullscreen experience
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed(2000){
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}
