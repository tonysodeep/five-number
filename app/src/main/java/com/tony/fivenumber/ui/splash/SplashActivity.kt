package com.tony.fivenumber.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.tony.fivenumber.R
import com.tony.fivenumber.base.BaseActivity
import com.tony.fivenumber.ui.welcome.WelcomeScreen
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    lateinit var handler : Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}