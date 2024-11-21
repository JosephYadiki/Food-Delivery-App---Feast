package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Bind views
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerTextView)

        checkIfAlreadyLoggedIn()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkIfAlreadyLoggedIn() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToHomePage(currentUser)
        }
    }

    // Function to login the user
    private fun loginUser(email: String, password: String) {
        // Show progress bar while login


        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    // Login Success
                    val user = auth.currentUser
                    navigateToHomePage(user)
                } else {
                    // If login fails, show a toast with the error message
                    Toast.makeText(this, "Authentication failed. ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Function to navigate to Home Activity (or any other screen after successful login)
    private fun navigateToHomePage(user: FirebaseUser?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("USER_ID", user?.uid)
        startActivity(intent)
        finish()  // Close the LoginActivity so the user can't return to it
    }
}
