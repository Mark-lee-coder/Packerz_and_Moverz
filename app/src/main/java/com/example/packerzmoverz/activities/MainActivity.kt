package com.example.packerzmoverz.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.packerzmoverz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
//    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    val user: FirebaseUser = firebaseAuth.currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coroutineScope.launch {
            delay(3000)
            startOnboarding()
        }
    }

    private fun startOnboarding() {
        val intent = Intent(this, Onboarding1::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()  // Cancel the coroutine when the activity is destroyed
    }
}