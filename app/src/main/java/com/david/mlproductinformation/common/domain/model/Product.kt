package com.david.mlproductinformation.common.domain.model

data class Product(
    val id: String,
    val title: String,
    val price: Int,
    val thumbnail: String,
    val installments: Installment,
    val detailsUrl: String,
    val shipping: Shipping
)