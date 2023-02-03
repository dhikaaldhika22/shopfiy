package com.myproject.app.shopify.api

import com.myproject.app.shopify.data.response.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun getProducts(
    ): ArrayList<Product>

    @GET("products/{id}")
    suspend fun getDetail(
        @Path("id") id: Int
    ): Product
}