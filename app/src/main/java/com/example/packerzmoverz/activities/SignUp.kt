package com.example.packerzmoverz.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.postDelayed
import com.example.packerzmoverz.R
import com.google.android.material.snackbar.Snackbar

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val login: TextView = findViewById(R.id.log_in)
        val signUp: Button = findViewById(R.id.sign_up)
        val etName: EditText = findViewById(R.id.etName)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val terms: CheckBox = findViewById(R.id.checkbox)
        val etPNumber: EditText = findViewById(R.id.etPNumber)

        setSupportActionBar(toolbar)

        login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        signUp.setOnClickListener {
            val textName = etName.text.toString().trim()
            val textEmail = etEmail.text.toString().trim()
            val textPassword = etPassword.text.toString()
            val textPNumber = etPNumber.text.toString().trim()

            if (textName.isEmpty()) {
                etName.error = "Please enter your name!"
                etName.requestFocus()
            }

            else if (textEmail.isEmpty()){
                etEmail.error = "Please enter your email!"
                etEmail.requestFocus()
            }

            else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                etEmail.error = "Please provide a valid email!"
                etEmail.requestFocus()
            }

            else if (textPNumber.isEmpty()) {
                etPNumber.error = "Please enter your phone number!"
                etPNumber.requestFocus()
            }

            else if (textPNumber.length != 10 ) {
                etPNumber.error = "Phone number should have 10 characters!"
                etPNumber.requestFocus()
            }

            else if (textPassword.isEmpty()) {
                etPassword.error = "Please enter your password!"
                etPassword.requestFocus()
            }

            else {
                fun isPasswordValid(password: String): Boolean {
                    val minLength = 8
                    val hasUppercase = password.any { it.isUpperCase() }
                    val hasLowercase = password.any { it.isLowerCase() }
                    val hasDigit = password.any { it.isDigit() }
                    val hasSpecial = password.any { !it.isLetterOrDigit() }

                    return password.length >= minLength &&
                            hasUppercase &&
                            hasLowercase &&
                            hasDigit &&
                            hasSpecial
                }

                if (!isPasswordValid(textPassword)) {
                    etPassword.error = "Please provide a strong password!"
                    etPassword.requestFocus()
                }

                else if (!terms.isChecked) {
                    val snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Kindly agree to Terms and Conditions to continue",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackbar.show()

                    val handler = Handler()
                    handler.postDelayed({ snackbar.dismiss() }, 5000)
                }

                else {
                    val snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Registered Successfully!",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackbar.show()

                    val handler = Handler()
                    handler.postDelayed({
                        snackbar.dismiss()
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
                }
            }
        }
    }
}