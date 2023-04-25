package com.david.mlproductinformation.common.data.api.model.mappers


import com.david.mlproductinformation.common.data.api.model.ApiShipping
import com.david.mlproductinformation.common.domain.model.Shipping
import javax.inject.Inject

class ApiShippingMapper @Inject constructor(): ApiMapper<ApiShipping?, Shipping> {
    override fun mapToDomain(apiEntity: ApiShipping?): Shipping {
        return Shipping(
            freeShipping = apiEntity?.freeShipping ?: false
        )
    }
}