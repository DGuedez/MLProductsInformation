package com.david.mlproductinformation.search.domain.repositories

interface ProductRepository {
    suspend fun storeProduct(product:String)
}