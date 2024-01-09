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
            val textEmail = user.email
            val query = FirebaseDatabase.getInstance().reference.child("TableUsers")
                .orderByChild("userEmail").equalTo(textEmail)

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(this@MainActivity, "Welcome Back!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, Home::class.java)
                        startActivity(intent)
                        finish()
                    }

                    else {
                        val intent = Intent(this@MainActivity, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
            })
        }

        else {
            val intent = Intent(this@MainActivity, Onboarding1::class.java)
            startActivity(intent)
            finish()
        }
    }
}