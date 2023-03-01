package com.android.jsjstore.model

data class Product(
    var title: String? = "",
    var picture: String= "",
    var description: String? = "",
    var price: Double? = 0.0,
    var rank: Int = 0,
    var category: String? = "",
    var numberInCart: String?= "",
    var mainPicture: String? = "",
)
