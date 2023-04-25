package com.david.mlproductinformation.common.data.api.model.mappers


import com.david.mlproductinformation.common.data.api.model.ApiProduct
import com.david.mlproductinformation.common.data.api.model.ApiSearchProduct
import com.david.mlproductinformation.common.domain.model.Product
import com.david.mlproductinformation.common.domain.model.RequestProduct
import javax.inject.Inject

class ApiRequestProductMapper @Inject constructor(private val apiProductMapper: ApiProductMapper) : ApiMapper<ApiSearchProduct, RequestProduct> {
    override fun mapToDomain(apiEntity: ApiSearchProduct): RequestProduct {
        return RequestProduct(
            query = apiEntity.query.orEmpty(),
            results = parseProductsList(apiEntity.productResults)
        )
    }

    private fun parseProductsList(apiEntity: List<ApiProduct>?): List<Product> {
      return  apiEntity?.map { product -> apiProductMapper.mapToDomain(product)  } ?: listOf()
    }
}
