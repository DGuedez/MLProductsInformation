package com.david.mlproductinformation.common.data.preferences

interface Preferences {

   fun putProductSearch(query:String)

   fun getProductSearchQuery():String

   fun putProductDetailUrl(keyProductUrl:String)

   fun getProductDetailUrl():String
}