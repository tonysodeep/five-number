package com.tony.fivenumber.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient
import com.tony.fivenumber.MyPreferences
import com.tony.fivenumber.R
import com.tony.fivenumber.base.BaseActivity
import com.tony.fivenumber.ui.welcome.WelcomeScreen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates
import kotlin.random.Random


class MainActivity : BaseActivity() {
    lateinit var mAdView : AdView
    var arrayListResultChoice = ArrayList<Int>()
    var arrayListResult = ArrayList<Int>()
    var arrayListColorButton = ArrayList<Int>()
    private lateinit var myPreferences: MyPreferences
    private var mp: MediaPlayer? = null
    private var mp2: MediaPlayer? = null
    var score: Int = 0
    var end = 5
    var start = 1
    lateinit var timer: CountDownTimer
    var myColorMap = mapOf<Int, Int>(
        R.drawable.border_bt_1 to R.drawable.background_bt_1,
        R.drawable.border_bt_2 to R.drawable.background_bt_2,
        R.drawable.border_bt_3 to R.drawable.background_bt_3,
        R.drawable.border_bt_4 to R.drawable.background_bt_4,
        R.drawable.border_bt_5 to R.drawable.background_bt_5,
    )
    var btarraycolor = arrayListOf(
        R.drawable.border_bt_1,
        R.drawable.border_bt_2,
        R.drawable.border_bt_3,
        R.drawable.border_bt_4,
        R.drawable.border_bt_5,
    )

    var buttonsNum = ArrayList<Button>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myPreferences = MyPreferences(this)
        bt_vt1.visibility = View.GONE
        bt_vt2.visibility = View.GONE
        bt_vt3.visibility = View.GONE
        bt_vt4.visibility = View.GONE
        bt_vt5.visibility = View.GONE
        main_screen.setOnClickListener {
            startGame()
            begin_text.visibility = View.GONE
            main_screen.isClickable = false
            bt_vt1.visibility = View.VISIBLE
            bt_vt2.visibility = View.VISIBLE
            bt_vt3.visibility = View.VISIBLE
            bt_vt4.visibility = View.VISIBLE
            bt_vt5.visibility = View.VISIBLE
        }
        playSound(R.raw.ingamemusic)
        buttonsNum = arrayListOf(bt_vt1, bt_vt2, bt_vt3, bt_vt4, bt_vt5)
        bt_vt1.setOnClickListener {
            buttonClickEvent(0)
        }
        bt_vt2.setOnClickListener {
            buttonClickEvent(1)
        }
        bt_vt3.setOnClickListener {
            buttonClickEvent(2)
        }
        bt_vt4.setOnClickListener {
            buttonClickEvent(3)
        }
        bt_vt5.setOnClickListener {
            buttonClickEvent(4)
        }

        MobileAds.initialize(this){

        }
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

    }

    private fun playSound(id: Int) {
        if (!myPreferences.getClickState()) {
            mp = MediaPlayer.create(this, id)
            if (id == R.raw.ingamemusic) {
                mp2 = MediaPlayer.create(this, id)
                mp2?.apply {
                    start()
                    isLooping = true

                }
            } else {
                mp?.apply {
                    start()
                    setVolume(10f, 10f)
                    isLooping = false
                }
            }
        }
    }

    fun buttonClickEvent(i: Int) {
        arrayListResultChoice.add(buttonsNum[i].text.toString().toInt())
        Log.d("ArrayChoice", arrayListColorButton.toString())
        buttonsNum[i].setBackgroundResource(
            myColorMap[arrayListColorButton[i]] ?: error("no value")
        )
        if (!checkResultOfArray()) {
            setBackGroundToAllButton()
        } else {
            buttonsNum[i].isClickable = false
            playSound(R.raw.clickbutton)
        }
        if (arrayListResultChoice.size == 5) {
            timer.cancel()
            timer.onFinish()
        }

    }

    fun setBackGroundToAllButton() {
        bt_vt1.setBackgroundResource(arrayListColorButton[0])
        bt_vt2.setBackgroundResource(arrayListColorButton[1])
        bt_vt3.setBackgroundResource(arrayListColorButton[2])
        bt_vt4.setBackgroundResource(arrayListColorButton[3])
        bt_vt5.setBackgroundResource(arrayListColorButton[4])
        playSound(R.raw.errorsound)
        bt_vt1.isClickable = true
        bt_vt2.isClickable = true
        bt_vt3.isClickable = true
        bt_vt4.isClickable = true
        bt_vt5.isClickable = true
        arrayListResultChoice.clear()
    }

    fun checkResultOfArray(): Boolean {
        for (i in 0 until arrayListResultChoice.size) {
            if (arrayListResultChoice[i] != arrayListResult[i]) {
                return false
            }
        }
        return true
    }

    fun isEqual(first: ArrayList<Int>, second: ArrayList<Int>): Boolean {
        first.forEachIndexed { index, value ->
            if (second[index] != value) {
                return false
            }
        }

        return true
    }

    fun isEqualsize(first: ArrayList<Int>, second: ArrayList<Int>): Boolean {
        if (first.size == second.size)
            return true
        return false
    }


    private fun startGame() {
        getFiveRandomNumNoTheSame(1, end)
        bt_vt1.isClickable = true
        bt_vt2.isClickable = true
        bt_vt3.isClickable = true
        bt_vt4.isClickable = true
        bt_vt5.isClickable = true
        progress_bar.max = 5000
        timer = object : CountDownTimer(5000, 1) {
            override fun onTick(millisUntilFinished: Long) {
                var progress = millisUntilFinished.toInt()
                progress_bar.progress = progress_bar.max - progress
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                bt_vt1.isClickable = false
                bt_vt2.isClickable = false
                bt_vt3.isClickable = false
                bt_vt4.isClickable = false
                bt_vt5.isClickable = false
                if (isEqual(arrayListResultChoice, arrayListResult) && isEqualsize(
                        arrayListResultChoice,
                        arrayListResult
                    )
                ) {
                    mp2?.pause()
                    playSound(R.raw.correctsound)
                    runBlocking {
                        delay(100)
                    }
                    mp2?.start()
                    score++
                    tv_score.text = "SCORE: $score"
                    arrayListResultChoice.clear()
                    bt_vt1.isClickable = true
                    bt_vt2.isClickable = true
                    bt_vt3.isClickable = true
                    bt_vt4.isClickable = true
                    bt_vt5.isClickable = true
                    end += 5
                    if((end - start)+1 > 50 ){
                        start += 50
                    }
                    getFiveRandomNumNoTheSame(start, end)
                    this.start()
                } else {
                    end = 5
                    start = 1
                    mp2?.stop()
                    playSound(R.raw.sad)

                    if (myPreferences.getHighScore() < score) {
                        GoogleSignIn.getLastSignedInAccount(this@MainActivity)?.let {
                            Games.getLeaderboardsClient(this@MainActivity, it)
                                .submitScore(getString(R.string.leaderbroadID), score.toLong())
                        }
                        myPreferences.setHighScore(score.toLong())
                    }
                    dialogBuilder()
                }
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mp2?.stop()
        timer.cancel()
    }

    @SuppressLint("SetTextI18n")
    fun dialogBuilder() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Bạn chơi gà quá choi duoc $score diem")
        val mAlterDialog = mBuilder.show()
        mAlterDialog.setCancelable(false)
        mAlterDialog.window?.setBackgroundDrawableResource(R.drawable.border_dialog)
        mDialogView.bt_retry.setOnClickListener {
            score = 0
            tv_score.text = "SCORE: 0"
            mAlterDialog.dismiss()
            arrayListResultChoice.clear()
            playSound(R.raw.ingamemusic)
            startGame()
        }
        mDialogView.home.setOnClickListener {
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            arrayListResultChoice.clear()
            mAlterDialog.dismiss()
            mp?.stop()
            finish()
        }
    }

    private fun rand(start: Int, end: Int): Int {
        require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
        return Random(System.nanoTime()).nextInt(end - start + 1) + start
    }

    fun getFiveRandomNumNoTheSame(start: Int, end: Int) {
        arrayListResult.clear()
        arrayListColorButton.clear()
        val buttonsNum = arrayOf(bt_vt1, bt_vt2, bt_vt3, bt_vt4, bt_vt5)
        var randomColor: Int = 0
        val arrayCopyColor = ArrayList<Int>()
        arrayCopyColor.addAll(btarraycolor)
        for (i in 1..5) {
            if (arrayListResult.isEmpty()) {
                arrayListResult.add(rand(start, end))
            } else {
                var a = 0
                while (true) {
                    var count = 0
                    a = rand(start, end)
                    for (i in 0 until arrayListResult.size) {
                        if (arrayListResult[i] == a) {
                            count = 1
                            break
                        }
                    }
                    if (count == 0) {
                        arrayListResult.add(a)
                        break
                    }
                }
            }
        }
        for (i in 0 until 5) {
            buttonsNum[i].text = arrayListResult[i].toString()
            randomColor = Random.nextInt(arrayCopyColor.size)
            arrayListColorButton.add(arrayCopyColor[randomColor])
            buttonsNum[i].setBackgroundResource(arrayCopyColor[randomColor])
            arrayCopyColor.removeAt(randomColor)
        }
        arrayListResult.sort()
    }
}
