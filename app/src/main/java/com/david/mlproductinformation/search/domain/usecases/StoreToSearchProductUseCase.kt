package com.david.mlproductinformation.search.domain.usecases

import com.david.mlproductinformation.common.domain.repositories.ProductRepository
import javax.inject.Inject

class StoreToSearchProductUseCase @Inject constructor (
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productQuery: String) {
        repository.storeProductSearchQuery(productQuery)
    }

}