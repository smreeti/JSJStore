package com.android.jsjstore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "client_orders")
data class ClientOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productName: String,
    val quantity: Int,
    val price: Double,
    val productImage: String,
)
