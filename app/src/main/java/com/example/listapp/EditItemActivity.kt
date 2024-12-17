package com.example.listapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameEditText = findViewById<EditText>(R.id.nameval)
        val dateEditText = findViewById<EditText>(R.id.DateVal)
        val quantityEditText = findViewById<EditText>(R.id.QuantityVal)
        val priceEditText = findViewById<EditText>(R.id.PriceVal)
        val save = findViewById<Button>(R.id.editBtn)

        // Get item details from intent
        val originalName = intent.getStringExtra("originalName")
        val currentName = intent.getStringExtra("name") ?: ""
        val currentBuyBefore = intent.getStringExtra("buyBefore") ?: ""
        val currentQuantity = intent.getIntExtra("quantity", 0)
        val currentPrice = intent.getDoubleExtra("price", 0.0)

        // Populate fields with current values
        nameEditText.setText(currentName)
        dateEditText.setText(currentBuyBefore)
        quantityEditText.setText(currentQuantity.toString())
        priceEditText.setText(currentPrice.toString())

        save.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedBuyBefore = dateEditText.text.toString()
            val updatedQuantity = quantityEditText.text.toString().toIntOrNull() ?: 0
            val updatedPrice = priceEditText.text.toString().toDoubleOrNull() ?: 0.0

            val resultIntent = Intent().apply {
                putExtra("action", "edit")
                putExtra("originalName", originalName) // Send original name for identification
                putExtra("name", updatedName)
                putExtra("buyBefore", updatedBuyBefore)
                putExtra("quantity", updatedQuantity)
                putExtra("price", updatedPrice)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        val homeButton = findViewById<Button>(R.id.home)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val themeSettingsButton = findViewById<Button>(R.id.themeSettingsButton)
        themeSettingsButton.setOnClickListener {
            val intent = Intent(this, ThemeSettingsActivity::class.java)
            startActivity(intent)
        }


        val webViewButton = findViewById<Button>(R.id.help)
        webViewButton.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }
    }
}