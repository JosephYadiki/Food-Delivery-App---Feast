package com.example.finalproject

data class CartItem(
    val id: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val imageResource: String = "",
    var quantity: Int = 1
)
