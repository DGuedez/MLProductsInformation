package com.david.mlproductinformation.products.presentation.mappers

import com.david.mlproductinformation.common.domain.model.Installment
import com.david.mlproductinformation.common.domain.model.Product
import com.david.mlproductinformation.common.presentation.UIMapper
import com.david.mlproductinformation.products.presentation.model.UIProduct
import javax.inject.Inject

class UIProductMapper @Inject constructor() : UIMapper<Product, UIProduct> {
    override fun mapToView(input: Product): UIProduct {
        return UIProduct(
            id = input.id,
            title = input.title,
            price = input.price.toString(),
            thumbnail = input.thumbnail,
            installmentQuantity = getInstallmentQuantity(input),
            InstallmentAmount = getInstallmentAmount(input),
            productFreeShipping = input.shipping.freeShipping,
            detailUrlLink = input.detailsUrl
        )
    }

    private fun getInstallmentAmount(input: Product): String {
        return when (val installment = input.installments) {
            is Installment.KNOWN -> installment.amount.toString()
            Installment.UNKNOWN -> " "
        }
    }

    private fun getInstallmentQuantity(input: Product): String {
        return when (val installment = input.installments) {
            is Installment.KNOWN -> installment.quantity.toString()
            Installment.UNKNOWN -> ""
        }
    }
}