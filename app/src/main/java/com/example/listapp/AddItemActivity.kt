package com.example.listapp
import android.app.DatePickerDialog
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class AddItemActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        // Initialize views
        nameEditText = findViewById(R.id.nameval)
        dateEditText = findViewById(R.id.DateVal)
        quantityEditText = findViewById(R.id.QuantityVal)
        priceEditText = findViewById(R.id.PriceVal)
        addButton = findViewById(R.id.editBtn)
        cancelButton = findViewById(R.id.cancelbtn)

        // Set up DatePicker for dateEditText
        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        cancelButton.setOnClickListener(){
            val intent = Intent(this, ListMainActivity::class.java)
            finish()
        }

        // Add button click listener
        addButton.setOnClickListener {
            addItem()
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
            dateEditText.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun addItem() {
        val name = nameEditText.text.toString().trim()
        val date = dateEditText.text.toString().trim()
        val quantityText = quantityEditText.text.toString().trim()
        val priceText = priceEditText.text.toString().trim()

        if (name.isEmpty() || date.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val quantity: Int
        val price: Double
        try {
            quantity = quantityText.toInt()
            price = priceText.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid quantity or price format!", Toast.LENGTH_SHORT).show()
            return
        }

        // Pass values back to MainActivity
        val resultIntent = Intent().apply {
            putExtra("action", "add")
            putExtra("name", name)
            putExtra("buyBefore", date)
            putExtra("quantity", quantity)
            putExtra("price", price)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}