package com.example.listapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val PREFS_NAME = "GroceryPrefs"
    private val TOKEN_KEY = "currentListToken"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (isListTokenPresent()) {
            // Go directly to ListMainActivity
            val intent = Intent(this, ListMainActivity::class.java)
            val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            intent.putExtra("TOKEN", prefs.getString(TOKEN_KEY, null))
            startActivity(intent)
            finish() // Close MainActivity
        } else {
            // Show default view
            setContentView(R.layout.activity_main)
        }

        val proceed = findViewById<Button>(R.id.button)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val Email = findViewById<EditText>(R.id.Email)
        val Password = findViewById<EditText>(R.id.Password)

        proceed.setOnClickListener(){

            if (Email.text.toString().isNotBlank()&&Password.text.toString().isNotBlank()) {

                val email = Email.text.toString()
                val password = Password.text.toString()

                // Save User Credentials (Create User)
                if (!isEmailRegistered(this, email)) {
                    saveUserCredentials(this, email, password)
                    Toast.makeText(this, "User created successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Email already registered!", Toast.LENGTH_SHORT).show()
                }

                // Authenticate User
                val isAuthenticated = authenticateUser(this, email, password)
                if (isAuthenticated) {
                    val intent = Intent(this, TokenActivity::class.java)
                    intent.putExtra("USER_EMAIL", Email.text.toString())
                    intent.putExtra("USER_PASSWORD", Password.text.toString())// Pass email as an extra
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show()
                }



            }

        }
    }

    private fun isListTokenPresent(): Boolean {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(TOKEN_KEY)
    }

    fun saveUserCredentials(context: Context, email: String, password: String) {
        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        // Save email-password pair
        editor.putString("email_$email", password) // Key: "email_<email>", Value: password
        editor.apply()
        Log.d("Auth", "User credentials saved for email: $email")
    }

    fun authenticateUser(context: Context, email: String, password: String): Boolean {
        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Retrieve password for the given email
        val savedPassword = prefs.getString("email_$email", null)

        return if (savedPassword != null && savedPassword == password) {
            Log.d("Auth", "Authentication successful for email: $email")
            true
        } else {
            Log.d("Auth", "Authentication failed for email: $email")
            false
        }
    }

    fun isEmailRegistered(context: Context, email: String): Boolean {
        val prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        // Check if there is a password associated with this email
        val isRegistered = prefs.contains("email_$email")
        Log.d("Auth", "Email registration check for $email: $isRegistered")
        return isRegistered
    }

}