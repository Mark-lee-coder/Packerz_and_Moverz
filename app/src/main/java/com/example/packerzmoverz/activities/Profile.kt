package com.example.packerzmoverz.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.packerzmoverz.R
import com.example.packerzmoverz.constructors.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.IOException

class Profile : AppCompatActivity() {
    private var backPressedOnce = false
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userID: String
    private lateinit var storageReference: StorageReference
    private lateinit var progressDialog: ProgressDialog
    private var profileImage: de.hdodenhof.circleimageview.CircleImageView = findViewById(R.id.profileImage)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.navigation_profile

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_bookings -> {
                    val intent = Intent(this@Profile, Bookings::class.java)
                    startActivity(intent)
                    finish()

                    true
                }

                R.id.navigation_home -> {
                    val intent = Intent(this@Profile, Home::class.java)
                    startActivity(intent)
                    finish()

                    true
                }

                else -> false
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("TableUsers")
        userID = user.uid

        databaseReference.child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue(User::class.java)

                if (userProfile != null) {
                    val tvName: TextView = findViewById(R.id.tvName)
                    val tvEmail: TextView = findViewById(R.id.tvEmail)
                    val tvPhoneNumber: TextView = findViewById(R.id.tvPhoneNumber)

                    val name = userProfile.UserName
                    val email = userProfile.UserEmail
                    val number = userProfile.PhoneNumber

                    tvName.text = name
                    tvEmail.text = email
                    tvPhoneNumber.text = number
                }

                else {
                    Toast.makeText(this@Profile, "No records found!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Profile, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        })

        storageReference = FirebaseStorage.getInstance().reference

        val profileReference = storageReference.child("TableUsers/${firebaseAuth.currentUser!!.uid}")

        profileReference.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(profileImage)
        }

        val editPic: ImageView = findViewById(R.id.editPic)

        editPic.setOnClickListener {
            val options = arrayOf<CharSequence>("Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose your profile picture")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Choose from Gallery" -> {
                        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, 1)
                    }

                    options[item] == "Cancel" -> dialog.dismiss()
                }
            }

            builder.show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            val imageUri = data?.data
            if (requestCode == 0) {
                if (resultCode == RESULT_OK && data != null) {
                    val selectedImage = data.extras!!["data"] as Bitmap
                    profileImage.setImageBitmap(selectedImage)
                }
            }

            imageUri?.let { uploadImageToFirebase(it) }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            val bout = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP, 75, bout)
            val bytes = bout.toByteArray()
            bout.close()

            val fileRef = storageReference.child("TableUsers/${firebaseAuth.currentUser!!.uid}")
            val uploadTask = fileRef.putBytes(bytes)
            progressDialog = ProgressDialog(this)
            progressDialog.show()
            progressDialog.setContentView(R.layout.progress_dialog)
            progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            uploadTask.addOnSuccessListener {
                Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                fileRef.downloadUrl.addOnSuccessListener {
                    Glide.with(this).load(it).into(profileImage)
                }

                progressDialog.dismiss()

            } .addOnFailureListener { _ ->
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

        catch (e: IOException) {
            e.printStackTrace()
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