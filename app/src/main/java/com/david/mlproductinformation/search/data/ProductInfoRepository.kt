package com.david.mlproductinformation.search.data


import com.david.mlproductinformation.search.data.preferences.Preferences
import com.david.mlproductinformation.search.domain.repositories.ProductRepository
import javax.inject.Inject

class ProductInfoRepository  @Inject constructor(
    private val preferences: Preferences
) : ProductRepository {

    override suspend fun storeProduct(product: String) {
        preferences.putProductSearch(product)
    }


}