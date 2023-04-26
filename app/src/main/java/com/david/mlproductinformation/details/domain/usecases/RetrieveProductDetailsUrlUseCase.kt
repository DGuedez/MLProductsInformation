package com.david.mlproductinformation.details.domain.usecases

import com.david.mlproductinformation.common.domain.repositories.ProductRepository
import javax.inject.Inject

class RetrieveProductDetailsUrlUseCase @Inject constructor( private val productRepository: ProductRepository) {
    suspend operator fun invoke(): String = productRepository.getProductDetails()
}
