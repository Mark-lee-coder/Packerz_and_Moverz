package com.example.packerzmoverz.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.packerzmoverz.R
import com.google.android.material.snackbar.Snackbar

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val signIn: Button = findViewById(R.id.sign_in)
        val forgotPass: TextView = findViewById(R.id.forgot_pass)
        val signUp: TextView = findViewById(R.id.sign_up)

        setSupportActionBar(toolbar)

        /**setting visibility of password**/
        etPassword.transformationMethod = PasswordTransformationMethod.getInstance() // Start with hidden password

        signIn.setOnClickListener {
            val textEmail: String = etEmail.text.toString().trim()
            val textPassword = etPassword.text.toString()

            if (textEmail.isEmpty()) {
                etEmail.error = "Please enter your email!"
                etEmail.requestFocus()
            }

            else if (textPassword.isEmpty()) {
                etPassword.error = "Please enter your password!"
                etPassword.requestFocus()
            }

            else {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }
        }

        forgotPass.setOnClickListener {
            val textEmail = etEmail.text.toString().trim()

            if (textEmail.isEmpty()) {
                etEmail.error = "Please enter your email!"
                etEmail.requestFocus()
            }

            else {
                val snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    "A password reset link has been sent to your email",
                    Snackbar.LENGTH_INDEFINITE
                )

                snackbar.show()
                // Dismiss the Snackbar after 5 seconds
                val handler = Handler()
                handler.postDelayed({ snackbar.dismiss() }, 5000)
            }
        }

        signUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}