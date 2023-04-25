package com.david.mlproductinformation.common.domain.model

data class RequestProduct(
    val query: String,
    val results: List<Product>
)