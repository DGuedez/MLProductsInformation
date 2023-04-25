package com.david.mlproductinformation.products.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.mlproductinformation.common.domain.NetworkException
import com.david.mlproductinformation.common.domain.NetworkUnavailableException
import com.david.mlproductinformation.common.presentation.Event
import com.david.mlproductinformation.common.utils.DispatchersProvider
import com.david.mlproductinformation.common.utils.createExceptionHandler
import com.david.mlproductinformation.products.domain.usecases.RequestProductsUseCase
import com.david.mlproductinformation.products.domain.usecases.StoreProductDetailsUrlUseCase
import com.david.mlproductinformation.products.presentation.mappers.UIProductMapper
import com.david.mlproductinformation.products.presentation.model.UIProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val requestProductUseCase: RequestProductsUseCase,
    private val uiProductMapper: UIProductMapper,
    private val storeProductDetailsUrlUseCase: StoreProductDetailsUrlUseCase
) : ViewModel() {

    val viewState: StateFlow<ProductViewState> get() = _viewState
    val viewEffect: SharedFlow<ProductViewEffects> get() = _viewEffect

    private val _viewState = MutableStateFlow(ProductViewState())
    private val _viewEffect = MutableSharedFlow<ProductViewEffects>()

    private val exceptionHandler = viewModelScope.createExceptionHandler { onFailure(it) }

    fun onEvent(event: ProductsEvents) {
        when (event) {
            ProductsEvents.RequestProductsFromSearch -> requestProducts()
            is ProductsEvents.RequestProductDetails -> storeProductDetailUrl(event.detailUrl)
        }
    }

    private fun storeProductDetailUrl(detailUrl: String) {
        viewModelScope.launch(exceptionHandler) {
            withContext(dispatchersProvider.io()) {
                storeProductDetailsUrlUseCase(detailUrl)
            }
        }
    }

    private fun requestProducts() {
        viewModelScope.launch(exceptionHandler) {
            generateLoadingState()
            val productsResult = withContext(dispatchersProvider.io()) { requestProductUseCase() }

            productsResult.map { uiProductMapper.mapToView(it) }.also { list ->
                generateProductListContentState(list)
            }

        }
    }

    private fun generateProductListContentState(list: List<UIProduct>) {
        if (list.isEmpty()) {
            upgradeNotFoundProductsState()
        } else {
            upgradeListContentState(list)
        }
    }

    private fun upgradeNotFoundProductsState() {
        _viewState.value = viewState.value.copy(
            loading = false,
            products = emptyList(),
            productsNotFound = true
        )
    }

    private fun upgradeListContentState(list: List<UIProduct>) {
        _viewState.value = viewState.value.copy(
            loading = false,
            products = list,
            productsNotFound = false
        )
    }

    private fun generateLoadingState() {
        _viewState.value = viewState.value.copy(
            loading = true,
            products = emptyList(),
            productsNotFound = false
        )
    }

    private fun onFailure(failure: Throwable) {
        when (failure) {
            is NetworkException,
            is NetworkUnavailableException -> {
                handleNetworkFailureEffect(failure)
            }
        }
    }

    private fun handleNetworkFailureEffect(sectionFailure: Throwable) {
        viewModelScope.launch(exceptionHandler) {
            _viewEffect.emit(
                ProductViewEffects.Failure(failure = Event(sectionFailure))
            )
        }
    }
}