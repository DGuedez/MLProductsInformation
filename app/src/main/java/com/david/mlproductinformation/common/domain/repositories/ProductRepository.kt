package com.david.mlproductinformation.common.domain.repositories

import com.david.mlproductinformation.common.domain.model.Product

interface ProductRepository {
    suspend fun storeProductSearchQuery(keyProduct:String)
    suspend fun getProducts() : List<Product>

    suspend fun storeProductDetailUrl(keyProductUrl:String)
}