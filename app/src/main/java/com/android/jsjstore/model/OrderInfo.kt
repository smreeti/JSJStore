package com.android.jsjstore.model

data class OrderInfo(
    var id: Long = 0,
    var productName: String = "",
    var quantity: Int = 0,
    var price: Double = 0.0,
    var productImage: String = "",
    var userId: String = "",
    var address: String = "",
    var creditCardNumber: String = "",
    var creditCartName: String = "",
    var creditCardExpirationDate: String = "",
    var cvv: String = ""
) {
    constructor() : this(0, "", 0, 0.0, "", "", "", "", "", "", "")
}
