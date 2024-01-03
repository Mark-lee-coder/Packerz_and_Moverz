package com.example.packerzmoverz.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.packerzmoverz.R
import com.example.packerzmoverz.constructors.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val login: TextView = findViewById(R.id.log_in)
        val signUp: Button = findViewById(R.id.sign_up)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etName: EditText = findViewById(R.id.etName)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val cbTerms: CheckBox = findViewById(R.id.cbTerms)
        val etTerms: TextView = findViewById(R.id.tvTerms)
        val etPNumber: EditText = findViewById(R.id.etPNumber)
        val firebaseAuth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar)

        login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        etTerms.setOnClickListener {
            val url = "https://www.google.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        signUp.setOnClickListener {
            val textName = etName.text.toString().trim()
            val textEmail = etEmail.text.toString().trim()
            val textPNumber = etPNumber.text.toString().trim()
            val textPassword = etPassword.text.toString()

            if (textName.isEmpty()) {
                etName.error = "Please enter your name!"
                etName.requestFocus()
            }

            else if (textEmail.isEmpty()) {
                etEmail.error = "Please enter your email!"
                etEmail.requestFocus()
            }

            else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                etEmail.error = "Please provide a valid email"
                etEmail.requestFocus()
            }

            else if (textPNumber.isEmpty()) {
                etPNumber.error = "Please enter your phone number!"
                etPNumber.requestFocus()
            }

            else {
                val countryCodePattern = """^\+\d{1,3}\d+${'$'}""".toRegex()

                if (!textPNumber.matches(countryCodePattern)) {
                    etPNumber.error = "Please provide a valid phone number with country code!"
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

                    else if (!cbTerms.isChecked) {
                        val snackbar = Snackbar.make(findViewById(android.R.id.content),
                            "Kindly agree to Terms and Conditions to continue",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        snackbar.show()

                        val handler = Handler()
                        handler.postDelayed({ snackbar.dismiss() }, 5000)
                    }

                    else {
                        val progressDialog = ProgressDialog(this@SignUp)
                        progressDialog.show()
                        progressDialog.setContentView(R.layout.progress_dialog)
                        progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(textPNumber) // User's phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration for the code sent
                            .setActivity(this) // Activity context
                            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                    // Phone number automatically verified or instant validation
                                    // Proceed with user creation or registration
                                    // Create user with phone number as an example
                                    val user = User(textName, textEmail, textPNumber, textPassword)

                                    // Add user details to the database
                                    FirebaseDatabase.getInstance().getReference("TableUsers")
                                        .child(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                                        .setValue(user)
                                        .addOnCompleteListener { databaseTask ->
                                            if (databaseTask.isSuccessful) {
                                                progressDialog.dismiss()
                                                val intent = Intent(this@SignUp, Login::class.java)
                                                startActivity(intent)
                                                finish()
                                            }

                                            else {
                                                progressDialog.dismiss()
                                                // Failed to add user details to the database
                                                Toast.makeText(this@SignUp, "User registration failed!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }

                                override fun onVerificationFailed(e: FirebaseException) {
                                    progressDialog.dismiss()
                                    // Handle verification failure
                                    // Display error messages or take appropriate actions
                                    Toast.makeText(this@SignUp, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                            .build()

                        // Step 2: Initiate phone number verification
                        PhoneAuthProvider.verifyPhoneNumber(options)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}