package com.david.mlproductinformation.common.data.api

import com.david.mlproductinformation.common.data.api.model.ApiSearchProduct
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIProductService {

    @GET("/sites/{siteId}/search")
    suspend fun requestProducts(
        @Path("siteId") siteId: String = ApiParameters.SITE_ID_MLC,
        @Query(ApiParameters.SEARCH_QUERY) query: String
    ): ApiSearchProduct
}