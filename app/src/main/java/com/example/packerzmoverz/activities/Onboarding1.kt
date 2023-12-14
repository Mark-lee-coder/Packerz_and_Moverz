package com.example.packerzmoverz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.packerzmoverz.R

class Onboarding1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding1)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val next: TextView = findViewById(R.id.next)
        next.setOnClickListener {
            val intent = Intent(this, Onboarding2::class.java)
            startActivity(intent)
        }

        val skip: TextView = findViewById(R.id.skip)
        skip.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}