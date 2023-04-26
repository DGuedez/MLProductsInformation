package com.david.mlproductinformation.common.data


import com.david.mlproductinformation.common.data.api.APIProductService
import com.david.mlproductinformation.common.data.api.model.mappers.ApiRequestProductMapper
import com.david.mlproductinformation.common.domain.NetworkException
import com.david.mlproductinformation.search.data.preferences.Preferences
import com.david.mlproductinformation.common.domain.repositories.ProductRepository
import com.david.mlproductinformation.common.domain.model.Product
import retrofit2.HttpException
import javax.inject.Inject

class ProductInfoRepository @Inject constructor(
    private val preferences: Preferences,
    private val apiProductService: APIProductService,
    private val apiRequestProductMapper: ApiRequestProductMapper
) : ProductRepository {

    override suspend fun storeProductSearchQuery(keyProduct: String) {
        preferences.putProductSearch(keyProduct)
    }

    override suspend fun getProducts(): List<Product> {
        try {
            val searchResult =
                apiRequestProductMapper.mapToDomain(apiProductService.requestProducts(query = preferences.getProductSearchQuery()))
            return searchResult.results
        } catch (exception: HttpException) {
            throw NetworkException(exception.message() ?: "Code${exception.code()}")
        }
    }

    override suspend fun storeProductDetailUrl(keyProductUrl: String) {
        preferences.putProductDetailUrl(keyProductUrl)
    }

    override suspend fun getProductDetails(): String = preferences.getProductDetailUrl()

}
