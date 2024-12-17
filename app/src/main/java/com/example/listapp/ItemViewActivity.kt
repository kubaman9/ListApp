package com.example.listapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class ItemViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = intent.getStringExtra("name") ?: "Unknown"
        val buyBefore = intent.getStringExtra("buyBefore") ?: "Unknown"
        val quantity = intent.getIntExtra("quantity", 0)
        val price = intent.getDoubleExtra("price", 0.0)

        // Initialize views
        val nameTextView = findViewById<TextView>(R.id.ItemNametxt)
        val dateTextView = findViewById<TextView>(R.id.textView4)
        val quantityTextView = findViewById<TextView>(R.id.textView5)
        val priceTextView = findViewById<TextView>(R.id.textView6)
        val edit = findViewById<Button>(R.id.editBtn)
        val remove = findViewById<Button>(R.id.button2)
        val back = findViewById<Button>(R.id.button3)

        // Populate views with item details
        nameTextView.text = "Name: $name"
        dateTextView.text = "Buy Before: $buyBefore"
        quantityTextView.text = "Quantity: $quantity"
        priceTextView.text = "Price: $${String.format("%.2f", price)}"

        edit.setOnClickListener(){
            val intent = Intent(this, EditItemActivity::class.java).apply {
                putExtra("originalName", intent.getStringExtra("name")) // Pass original name
                putExtra("name", intent.getStringExtra("name"))
                putExtra("buyBefore", intent.getStringExtra("buyBefore"))
                putExtra("quantity", intent.getIntExtra("quantity", 0))
                putExtra("price", intent.getDoubleExtra("price", 0.0))
            }
            startActivityForResult(intent, 2)
        }
        remove.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("action", "delete")
                putExtra("name", name)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        back.setOnClickListener(){
            val intent = Intent(this, ListMainActivity::class.java)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            // Forward the edit result back to ListMainActivity
            setResult(RESULT_OK, data)
            finish() // Close the detail view and go back to ListMainActivity
        }
    }
}