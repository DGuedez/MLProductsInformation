package com.david.mlproductinformation.products.presentation

import com.david.mlproductinformation.common.presentation.Event

sealed class ProductViewEffects {

    data class Failure(val failure : Event<Throwable>) :ProductViewEffects()

    object NavigateToProductDetails : ProductViewEffects()

}