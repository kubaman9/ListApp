package com.example.listapp

import android.os.Parcel
import android.os.Parcelable

data class Item(
    val name: String,
    val buyBefore: String,
    val quantity: Int,
    val price: Double
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "buyBefore" to buyBefore,
            "quantity" to quantity,
            "price" to price
        )
    }
}
