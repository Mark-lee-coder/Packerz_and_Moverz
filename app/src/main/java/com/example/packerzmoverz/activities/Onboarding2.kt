package com.example.packerzmoverz.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.packerzmoverz.R

class Onboarding2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding2)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val next: TextView = findViewById(R.id.next)
        next.setOnClickListener {
            val intent = Intent(this, Onboarding3::class.java)
            startActivity(intent)
        }

        val skip: TextView = findViewById(R.id.skip)
        skip.setOnClickListener {
            val intent = Intent(this, GetStarted::class.java)
            startActivity(intent)
            finish()
        }
    }
}