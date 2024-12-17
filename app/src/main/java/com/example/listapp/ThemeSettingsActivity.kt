package com.example.listapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class ThemeSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the current theme
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_settings)

        // Initialize theme buttons
        val lightThemeButton = findViewById<RadioButton>(R.id.lightThemeButton)
        val darkThemeButton = findViewById<RadioButton>(R.id.darkThemeButton)
        val saveButton = findViewById<Button>(R.id.saveThemeButton)

        saveButton.setOnClickListener {
            val selectedTheme = if (lightThemeButton.isChecked) {
                ThemeManager.THEME_LIGHT
            } else {
                ThemeManager.THEME_DARK
            }

            ThemeManager.saveTheme(this, selectedTheme)
            recreate() // Restart activity to apply theme

            val intent = Intent(this, ListMainActivity::class.java)
            finish()
        }
    }
}
