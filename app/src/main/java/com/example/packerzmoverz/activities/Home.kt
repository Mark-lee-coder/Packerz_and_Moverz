package com.example.packerzmoverz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.packerzmoverz.R

class Home : AppCompatActivity() {
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed()
            return
        }

        backPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        // Reset backPressedOnce after a certain time (e.g., 3 seconds) to allow a double press to exit
        android.os.Handler().postDelayed({
            backPressedOnce = false
        }, 3000)
    }
}