package com.david.mlproductinformation.search.presentation

sealed class SearchProductEvent  {
    data class RequestSearch(val query:String): SearchProductEvent()
}