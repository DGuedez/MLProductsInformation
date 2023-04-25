package com.david.mlproductinformation.products.presentation

sealed class ProductsEvents {

    object RequestProductsFromSearch : ProductsEvents()

    data class RequestProductDetails(val detailUrl: String): ProductsEvents()
}