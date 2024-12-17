package com.example.listapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import java.io.File

class TokenActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_token)

        val logoutBtn = findViewById<Button>(R.id.logout)
        val tokenEnter = findViewById<Button>(R.id.tokenEnter)
        val tokenValue = findViewById<EditText>(R.id.tokenValue)
        val newTokenEnter = findViewById<Button>(R.id.newtokenEnter)
        val newTokenValue = findViewById<EditText>(R.id.newtokenValue)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logoutBtn.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        tokenEnter.setOnClickListener() {
            if (tokenValue.text.toString().isNotBlank()) {
                val intent = Intent(this, ListMainActivity::class.java)
                intent.putExtra("TOKEN", tokenValue.text.toString())
                startActivity(intent)
                finish()
            }
        }

        newTokenEnter.setOnClickListener() {
            if (newTokenValue.text.toString().isNotBlank()) {
                val intent = Intent(this, ListMainActivity::class.java)
                intent.putExtra("TOKEN", generateUniqueFourDigitCombo())
                startActivity(intent)
                finish()
            }
        }
    }

    private var generatedNumbers = mutableSetOf<String>()

    fun generateUniqueFourDigitCombo(): String {
        generatedNumbers = fetchListsFromFile(this).toMutableSet()
        var combo: String
        do {
            combo = (1000..9999).random().toString() // Generate random 4-digit number
        } while (generatedNumbers.contains(combo)) // Ensure uniqueness
        generatedNumbers.add(combo) // Store the generated combo
        saveListsToFile(this, generatedNumbers.toList())
        return combo
    }

    private val gson = Gson()
    private val FILE_NAME = "grocery_lists.json"

    fun saveListsToFile(context: Context, lists: List<String>) {
        try {
            val json = gson.toJson(lists) // Convert list to JSON
            val file = File(context.filesDir, FILE_NAME)
            file.writeText(json) // Write JSON to file
            println("Lists saved successfully: $json")
        } catch (e: Exception) {
            println("Error saving lists: ${e.message}")
        }
    }

    fun fetchListsFromFile(context: Context): MutableList<String> {
        val file = File(context.filesDir, FILE_NAME)
        return if (file.exists()) {
            try {
                val json = file.readText() // Read JSON from file
                val listType = object : com.google.gson.reflect.TypeToken<MutableList<String>>() {}.type
                gson.fromJson(json, listType) ?: mutableListOf() // Convert JSON to List
            } catch (e: Exception) {
                println("Error fetching lists: ${e.message}")
                mutableListOf()
            }
        } else {
            println("File does not exist, returning empty list.")
            mutableListOf()
        }
    }

}