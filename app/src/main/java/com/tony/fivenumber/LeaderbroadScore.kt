package com.tony.fivenumber

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.games.AchievementsClient
import com.google.android.gms.games.Games
import com.google.android.gms.games.LeaderboardsClient

class LeaderbroadScore(context: Context) {
    private var googleSignInClient: GoogleSignInClient? = null
    private var leaderboardsClient: LeaderboardsClient? = null
    init {
        googleSignInClient = GoogleSignIn.getClient(
            context, GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN
            ).build()
        )
        googleSignInClient?.silentSignIn()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                leaderboardsClient = Games.getLeaderboardsClient(context, task.result!!)
            } else {
                Log.e("Error", "SignIn", task.exception)
            }
        }
    }
}