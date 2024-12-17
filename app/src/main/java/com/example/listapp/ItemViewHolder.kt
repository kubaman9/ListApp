package com.example.listapp

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
    private val buyBeforeDateTextView: TextView = itemView.findViewById(R.id.buyBeforeDateTextView)
    private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)

    fun bind(groceryItem: Item) {
        nameTextView.text = groceryItem.name
        quantityTextView.text = "Quantity: ${groceryItem.quantity}"
        buyBeforeDateTextView.text = "Buy Before: ${groceryItem.buyBefore}"
        priceTextView.text = "Price: $${String.format("%.2f", groceryItem.price)}"
    }
}