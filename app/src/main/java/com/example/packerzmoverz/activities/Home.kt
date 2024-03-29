package com.example.packerzmoverz.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.packerzmoverz.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.navigation_home

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_bookings -> {
                    val intent = Intent(this@Home, Bookings::class.java)
                    startActivity(intent)
                    finish()

                    // Deselect Home
                    bottomNavigationView.menu.findItem(R.id.navigation_home)?.isChecked = false

                    // Select Bookings
                    bottomNavigationView.menu.findItem(R.id.navigation_bookings)?.isChecked = true

                    // Deselect Profile
                    bottomNavigationView.menu.findItem(R.id.navigation_profile)?.isChecked = false

                    true
                }

                R.id.navigation_profile -> {
                    val intent = Intent(this@Home, Profile::class.java)
                    startActivity(intent)
                    finish()

                    true
                }

                else -> false
            }
        }
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