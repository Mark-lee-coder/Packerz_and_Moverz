package com.example.packerzmoverz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.packerzmoverz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null && user.isEmailVerified) {
            val email = user.email
            val query = FirebaseDatabase.getInstance().reference.child("TableUsers").orderByChild("email").equalTo(email)

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val intent = Intent(this@MainActivity, Home::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(applicationContext, "Welcome Back", Toast.LENGTH_SHORT).show()
                    }

                    else {
                        val intent = Intent(this@MainActivity, Onboarding1::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        else {
            val intent = Intent(applicationContext, Onboarding1::class.java)
            startActivity(intent)
            finish()
        }
    }
}