package com.david.mlproductinformation.products.presentation

import com.david.mlproductinformation.products.presentation.model.UIProduct

data class ProductViewState(
    val loading:Boolean = true,
    val products: List<UIProduct> = emptyList(),
    val productsNotFound: Boolean = false
) {
  val showList: Boolean
  get() =  products.isNotEmpty() && !productsNotFound

}