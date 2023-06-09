package com.david.mlproductinformation.common.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.david.mlproductinformation.common.data.preferences.PreferenceConstants.KEY_PRODUCT_URL
import com.david.mlproductinformation.common.data.preferences.PreferenceConstants.KEY_SEARCH_PRODUCT_QUERY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductInfoPreferences  @Inject constructor(
    @ApplicationContext context: Context
) : Preferences {

    companion object {
        const val PREFERENCES_NAME = "PRODUCT_SAVE_PREFERENCES"
    }

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private inline fun editAction(action: SharedPreferences.Editor.() -> Unit) {
        with(preferences.edit()) {
            action()
            commit()
        }
    }

    override fun putProductSearch(query: String) {
        editAction { putString(KEY_SEARCH_PRODUCT_QUERY, query) }
    }

    override fun getProductSearchQuery(): String {
        return preferences.getString(KEY_SEARCH_PRODUCT_QUERY, "").orEmpty()
    }

    override fun putProductDetailUrl(keyProductUrl: String) {
        editAction { putString( KEY_PRODUCT_URL, keyProductUrl) }
    }

    override fun getProductDetailUrl(): String {
        return preferences.getString(KEY_PRODUCT_URL, "").orEmpty()
    }
}