package com.tony.fivenumber.ui.welcome

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient
import com.tony.fivenumber.LeaderbroadScore
import com.tony.fivenumber.MyPreferences
import com.tony.fivenumber.R
import com.tony.fivenumber.ui.MainActivity
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.android.synthetic.main.activity_welcome_screen.*


class WelcomeScreen : AppCompatActivity() {
    private var leaderboardsClient: LeaderboardsClient? = null
    private var mp: MediaPlayer? = null
    private lateinit var myPreferences: MyPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        myPreferences = MyPreferences(this)
        mp = MediaPlayer.create(this, R.raw.homesound)
        LeaderbroadScore(this)
        if (!myPreferences.getClickState()) {
            bt_mute.setImageResource(R.drawable.mute_sound)
            mp?.apply {
                start()
                isLooping = true
            }
        } else {
            bt_mute.setImageResource(R.drawable.volume_sound)
        }
        bt_mute.setOnClickListener {
            if (!myPreferences.getClickState()) {
                mp?.pause()
                myPreferences.setClickState(true)
                bt_mute.setImageResource(R.drawable.volume_sound)
            } else {
                mp?.start()
                myPreferences.setClickState(false)
                bt_mute.setImageResource(R.drawable.mute_sound)
            }
        }

        play_button.setEventListener(object : SparkEventListener {
            override fun onEvent(button: ImageView?, buttonState: Boolean) {
                play_button.isChecked = true
            }

            override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {
                play_button.isChecked = false
                play_button.setInactiveImage(R.drawable.activeplay)
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onEventAnimationStart(button: ImageView?, buttonState: Boolean) {
                play_button.animate()
            }
        })

        bt_high_score.setOnClickListener {
            showTopPlayers()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.release()
    }

    override fun onPause() {
        super.onPause()
        mp?.pause()
    }

    override fun onRestart() {
        super.onRestart()
        mp?.start()
    }


    fun showTopPlayers() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
            .getLeaderboardIntent(getString(R.string.leaderbroadID))
            .addOnSuccessListener { intent -> startActivityForResult(intent, 10) }
    }

}