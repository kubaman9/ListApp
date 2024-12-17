package com.example.listapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class ListMainActivity : AppCompatActivity() {
    private val PREFS_NAME = "GroceryPrefs"
    private val KEY_GROCERY_LIST = "groceryList"
    private val gson = Gson()
    private val TOKEN_KEY = "currentListToken"

    private lateinit var adapter: ItemAdapter
    private val itemList = mutableListOf<Item>()
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_main)

        val tokentxt = findViewById<TextView>(R.id.token)
        val totaltxt = findViewById<TextView>(R.id.totaltxt)
        ThemeManager.applyTheme(this)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (intent.getStringExtra("TOKEN").toString().isNotBlank()){
            token = intent.getStringExtra("TOKEN").toString()
            tokentxt.setText("Token: " + token)
            setListToken(intent.getStringExtra("TOKEN").toString())
        }else {
            token = prefs.getString(TOKEN_KEY, null).toString()
            tokentxt.setText("Token: " + token)
        }
        setListToken(token)

        Log.v("MYERROR", "loaded back")
        loadGroceryList(token) // Load saved list from SharedPreferences

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ItemAdapter(itemList) { item ->
            openItemDetailActivity(item)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val addButton = findViewById<ImageButton>(R.id.Addbtn)
        addButton.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
        }

        val homeButton = findViewById<Button>(R.id.home)
        homeButton.setOnClickListener {
            clearListToken()
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

        totaltxt.setText("Total: " + calculateTotalCost().toString())

    }

    private fun setListToken(token: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    private fun clearListToken() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(TOKEN_KEY).apply() // Remove the token
    }

    private fun calculateTotalCost(): Double {
        var total = 0.0
        for (item in itemList) {
            total += item.quantity * item.price
        }
        return total
    }

    private fun openItemDetailActivity(item: Item) {
        val intent = Intent(this, ItemViewActivity::class.java).apply {
            putExtra("name", item.name)
            putExtra("buyBefore", item.buyBefore)
            putExtra("quantity", item.quantity)
            putExtra("price", item.price)
        }
        startActivityForResult(intent,1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { super.onActivityResult(requestCode, resultCode, data)
        Log.v("MYERROR", "loaded back")
        if (data != null) {
            val action = data.getStringExtra("action") ?: ""

            val name = data.getStringExtra("name") ?: return
            val buyBefore = data.getStringExtra("buyBefore") ?: ""
            val quantity = data.getIntExtra("quantity", 0)
            val price = data.getDoubleExtra("price", 0.0)
            val newItem = Item(name, buyBefore, quantity, price)
            Log.v("MYERROR", "got new Item")
            Log.v("MYERROR", newItem.name)
            when (action) {
                "add" -> addItemToList(newItem)
                "edit" -> editItemInList(data.getStringExtra("originalName"), newItem)
                "delete" -> deleteItemFromList(name)
            }
        }
        loadGroceryList(token)
        val totaltxt = findViewById<TextView>(R.id.totaltxt)
        totaltxt.setText("Total: " + calculateTotalCost().toString())
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        token = prefs.getString(TOKEN_KEY, null).toString()
        ThemeManager.applyTheme(this)
        recreate()
    }

    private fun addItemToList(item: Item) {
        itemList.add(item)
        adapter.notifyItemInserted(itemList.size - 1)
        saveGroceryList(token)
        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
    }

    private fun editItemInList(originalName: String?, editedItem: Item) {
        if (originalName == null) return
        val index = itemList.indexOfFirst { it.name == originalName }
        if (index != -1) {
            itemList[index] = editedItem
            adapter.notifyItemChanged(index)
            saveGroceryList(token)
            Toast.makeText(this, "Item edited", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteItemFromList(originalName: String?) {
        Log.v("MYERROR", "loaded delte")
        if (originalName == null) return
        val index = itemList.indexOfFirst { it.name == originalName }
        Log.v("MYERROR", "got index")
        if (index != -1) {
            Log.v("MYERROR", "almost")
            itemList.removeAt(index)
            adapter.notifyItemRemoved(index)
            saveGroceryList(token)
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveGroceryList(token: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        // Convert the list to JSON and save it using the token as the key
        val json = gson.toJson(itemList)
        editor.putString("LIST_$token", json) // Use the token to uniquely identify the list
        editor.apply()

        Log.d("MainActivity", "List saved for token $token: $json")
    }

    private fun loadGroceryList(token: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString("LIST_$token", null) // Use the token to fetch the specific list

        if (json != null) {
            // Convert the JSON string back to a list of items
            val type = object : TypeToken<MutableList<Item>>() {}.type
            val loadedList: MutableList<Item> = gson.fromJson(json, type)
            itemList.clear()
            itemList.addAll(loadedList)
            Log.d("MainActivity", "List loaded for token $token: $json")
        } else {
            Log.d("MainActivity", "No saved list found for token $token.")
        }
    }

    companion object {
        const val ADD_ITEM_REQUEST_CODE = 1
    }
}

