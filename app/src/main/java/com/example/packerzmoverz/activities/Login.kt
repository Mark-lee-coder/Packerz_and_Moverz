package com.example.packerzmoverz.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.packerzmoverz.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.integrity.e
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val cbRememberMe: CheckBox = findViewById(R.id.cbRememberMe)
        val signIn: Button = findViewById(R.id.sign_in)
        val forgotPass: TextView = findViewById(R.id.forgot_pass)
        val signUp: TextView = findViewById(R.id.sign_up)
        val firebaseAuth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar)

        /**setting visibility of password*/
        etPassword.transformationMethod = PasswordTransformationMethod.getInstance() // Start with hidden password

        signIn.setOnClickListener {
            val textPassword = etPassword.text.toString()



            if (textPassword.isEmpty()) {
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

            else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                etEmail.error = "Please provide a valid email"
                etEmail.requestFocus()
            }

            else {
                // Check if the email exists
                firebaseAuth.fetchSignInMethodsForEmail(textEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods ?: emptyList()

                        if (signInMethods.isNotEmpty()) {
                            // Email exists, send password reset link
                            firebaseAuth.sendPasswordResetEmail(textEmail).addOnCompleteListener { resetTask ->
                                    if (resetTask.isSuccessful) {
                                        val snackbar = Snackbar.make(findViewById(android.R.id.content),
                                            "A password reset link has been sent to your email",
                                            Snackbar.LENGTH_INDEFINITE
                                        )
                                        snackbar.show()
                                        // Dismiss the Snackbar after 5 seconds
                                        val handler = Handler()
                                        handler.postDelayed({ snackbar.dismiss() }, 5000)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this@Login, "Email for password reset not sent: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }

                        else {
                            // Email does not exist
                            Toast.makeText(this@Login, "Email does not exist in our records", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        signUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
}