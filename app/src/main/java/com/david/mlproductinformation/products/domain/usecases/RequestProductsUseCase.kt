package com.david.mlproductinformation.products.domain.usecases

import com.david.mlproductinformation.common.domain.model.Product
import com.david.mlproductinformation.common.domain.repositories.ProductRepository
import javax.inject.Inject

class RequestProductsUseCase @Inject constructor (private val productRepository: ProductRepository) {

    suspend operator fun invoke(): List<Product> =
        productRepository.getProducts()
}