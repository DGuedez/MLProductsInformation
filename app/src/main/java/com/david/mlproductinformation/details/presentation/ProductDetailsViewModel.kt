package com.david.mlproductinformation.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.mlproductinformation.common.utils.DispatchersProvider
import com.david.mlproductinformation.details.domain.usecases.RetrieveProductDetailsUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val retrieveProductDetailsUrlUseCase: RetrieveProductDetailsUrlUseCase
) : ViewModel() {

    val viewState: StateFlow<ProductDetailsViewState> get() = _viewState

    private val _viewState = MutableStateFlow(ProductDetailsViewState())

    fun onEvent(event: ProductDetailsEvents) {
        when (event) {
            ProductDetailsEvents.RetrieveProductProductDetails -> retrieveDetails()
        }
    }

    private fun retrieveDetails() {
        viewModelScope.launch {
            val detailsResult =
                withContext(dispatchersProvider.io()) { retrieveProductDetailsUrlUseCase() }
            updateDetails(detailsResult)
        }
    }

    private fun updateDetails(detailsResult: String) {
        _viewState.value = viewState.value.copy(
            details = detailsResult
        )
    }


}