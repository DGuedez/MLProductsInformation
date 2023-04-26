package com.david.mlproductinformation.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.mlproductinformation.common.utils.DispatchersProvider
import com.david.mlproductinformation.search.domain.usecases.StoreToSearchProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchProductsViewModel @Inject constructor(
    private val storeToSearchUseCase: StoreToSearchProductUseCase,
    private val dispatchersProvider: DispatchersProvider
): ViewModel() {


    val viewState: StateFlow<SearchProductViewState>  get() = _viewState
    val viewEffect: SharedFlow<SearchProductsViewEffects>  get() = _viewEffect

    private val _viewState  = MutableStateFlow(SearchProductViewState())
    private val _viewEffect  = MutableSharedFlow<SearchProductsViewEffects>()


    fun onEvent(event:SearchProductEvent){
        when(event){
            is SearchProductEvent.RequestSearch ->  handleQuery(event.query)
        }
    }

    private fun handleQuery(query: String) {
        processQuery()
        checkQuery(query)
    }

    private fun processQuery() {
        _viewState.value = viewState.value.copy(
            loading = true
        )
    }

    private fun checkQuery(query: String) {
        storeProduct(query)
    }

    private fun storeProduct(query: String) {

        viewModelScope.launch {
            withContext(dispatchersProvider.io()){ storeToSearchUseCase.invoke(query) }
            queryIsSaved()
           _viewEffect.emit(SearchProductsViewEffects.NavigateToSearchProductsList)
        }
    }

    private fun queryIsSaved() {
        _viewState.value = viewState.value.copy(
            loading = false
        )
    }

}