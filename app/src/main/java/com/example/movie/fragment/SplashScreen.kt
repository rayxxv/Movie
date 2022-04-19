package com.example.movie.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.movie.MainActivity
import com.example.movie.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash_screen)
        this.window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this,
                    MainActivity::class.java)
            )
            finish()
        }, 2500)
    }
}