package com.david.mlproductinformation.products.domain.usecases


import com.david.mlproductinformation.common.domain.repositories.ProductRepository
import javax.inject.Inject

class StoreProductDetailsUrlUseCase @Inject constructor (private val productRepository: ProductRepository) {
    suspend operator fun invoke(productUrl: String)  = productRepository.storeProductDetailUrl(productUrl)
}