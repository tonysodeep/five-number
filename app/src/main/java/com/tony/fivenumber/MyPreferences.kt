package com.tony.fivenumber

import android.content.Context
import android.content.SharedPreferences
import android.view.Display

class MyPreferences(context: Context) {
    val PREFERENCES_NAME = "SharePreferencesShould"
    val PREFERENCE_SOUND = "MuteButton"
    val PREFERNCE_SCORE = "HighestScore"
    val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE)

    fun getClickState():Boolean{
        return preferences.getBoolean(PREFERENCE_SOUND,false)
    }
    fun setClickState(state: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(PREFERENCE_SOUND,state)
        editor.apply()
    }
    fun getHighScore():Long{
        return preferences.getLong(PREFERNCE_SCORE,0)
    }
    fun setHighScore(score:Long){
        val editor = preferences.edit()
        editor.putLong(PREFERNCE_SCORE,score)
        editor.apply()
    }
}