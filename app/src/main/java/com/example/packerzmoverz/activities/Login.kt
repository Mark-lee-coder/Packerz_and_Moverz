package com.example.packerzmoverz.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.packerzmoverz.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

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
        val firebaseAuth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar)

        /**setting visibility of password*/
        etPassword.transformationMethod = PasswordTransformationMethod.getInstance() // Start with hidden password

        signIn.setOnClickListener {
            val textEmail = etEmail.text.toString().trim()
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
                val progressDialog = ProgressDialog(this@Login)
                progressDialog.show()
                progressDialog.setContentView(R.layout.progress_dialog)
                progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                firebaseAuth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (firebaseAuth.currentUser!!.isEmailVerified) {
                            val intent = Intent(this, Home::class.java)
                            startActivity(intent)
                            finish()
                        }

                        else {
                            progressDialog.dismiss()
                            val builder = AlertDialog.Builder(this@Login)
                            builder.setMessage("Email not verified! Resend the verification Email?")
                            builder.setPositiveButton("YES") { _, _ ->
                                firebaseAuth.currentUser!!.sendEmailVerification().addOnCompleteListener { resendTask ->
                                    if (resendTask.isSuccessful) {
                                        Toast.makeText(this, "Verification email sent", Toast.LENGTH_LONG).show()
                                    }

                                    else {
                                        Toast.makeText(this, "Verification email not sent", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } .setNegativeButton("NO") { _, _ ->

                            }
                            val alertDialog = builder.create()
                            alertDialog.show()
                        }
                    }

                    else {
                        progressDialog.dismiss()
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        etPassword.setText("")
                    }
                }
            }
        }

        forgotPass.setOnClickListener {
            val textEmail = etEmail.text.toString().trim()

            if (textEmail.isEmpty()) {
                etEmail.error = "Please enter your email!"
                etEmail.requestFocus()
            }

            else {
                firebaseAuth.sendPasswordResetEmail(textEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val snackbar = Snackbar.make(findViewById(android.R.id.content),
                            "Check your email for the link to reset your password",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        snackbar.show()

                        val handler = Handler()
                        handler.postDelayed({ snackbar.dismiss() }, 5000)
                    }
                } .addOnFailureListener { e ->
                    Toast.makeText(this@Login,"Email for password reset not sent: " + e.message, Toast.LENGTH_LONG).show()
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