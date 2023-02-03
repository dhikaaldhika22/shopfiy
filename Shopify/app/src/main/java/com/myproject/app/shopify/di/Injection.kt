package com.myproject.app.shopify.di

import android.content.Context
import com.myproject.app.shopify.api.ApiConfig
import com.myproject.app.shopify.data.repo.Repository
import com.myproject.app.shopify.data.room.ProductDatabase

object Injection {
    fun provideRepo(context: Context): Repository {
        val database = ProductDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        val productDao = database.productDao()

        return Repository.getInstance(apiService, productDao)
    }
}