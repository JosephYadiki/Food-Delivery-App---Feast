package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        nameTextView = findViewById(R.id.usernameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        logoutButton = findViewById(R.id.logoutButton)

        // Load user data
        loadUserData()

        // Set logout button click listener
        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Show user information
            nameTextView.text = currentUser.displayName ?: "No name available"
            emailTextView.text = currentUser.email ?: "No email available"
        }
    }

    private fun logoutUser() {
        auth.signOut()
        // Redirect to LoginActivity after logout
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Finish ProfileActivity to prevent going back
    }
}
