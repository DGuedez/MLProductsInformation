package com.david.mlproductinformation.common.data.api.model.mappers

import com.david.mlproductinformation.common.data.api.model.ApiInstallment
import com.david.mlproductinformation.common.domain.model.Installment
import javax.inject.Inject


class ApiInstallmentsMapper @Inject constructor() :
    ApiMapper<ApiInstallment?, Installment> {
    override fun mapToDomain(apiEntity: ApiInstallment?): Installment {
        return if (apiEntity != null) {
            Installment.KNOWN(
                quantity = apiEntity.quantity
                    ?: throw MappingException("Installment Quantity can't be null"),
                amount = apiEntity.amount
                    ?: throw MappingException("Installment Amount can't be null")
            )
        } else {
            Installment.UNKNOWN
        }
    }
}