package com.david.mlproductinformation.search.data.preferences

interface Preferences {

   fun putProductSearch(query:String)

   fun getProductSearchQuery():String
}