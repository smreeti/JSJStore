package com.android.jsjstore.model

data class OrderInfo(
    var id: Long = 0,
    var productName: String,
    var quantity: Int,
    var price: Double,
    var productImage: String,
    var userId: String,
    var address: String,
    var creditCardNumber: String,
    var creditCartName: String,
    var creditCardExpirationDate: String,
    var cvv: String
)
