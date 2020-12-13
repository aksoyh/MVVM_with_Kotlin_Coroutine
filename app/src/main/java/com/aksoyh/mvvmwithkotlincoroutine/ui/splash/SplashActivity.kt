package com.aksoyh.mvvmwithkotlincoroutine.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.aksoyh.mvvmwithkotlincoroutine.R
import com.aksoyh.mvvmwithkotlincoroutine.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            navigateToMain()
        }, 2000)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}