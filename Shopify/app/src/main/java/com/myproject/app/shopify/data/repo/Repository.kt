package com.myproject.app.shopify.data.repo

import android.util.Log
import com.myproject.app.shopify.api.ApiService
import com.myproject.app.shopify.data.response.Product
import com.myproject.app.shopify.data.response.ResultResponse
import com.myproject.app.shopify.data.room.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.ArrayList

class Repository private constructor(private val apiService: ApiService, private val productDao: ProductDao) {
    companion object {
        private val TAG = Repository::class.java.simpleName
        private var INSTANCE: Repository? = null

        fun getInstance(apiService: ApiService, productDao: ProductDao): Repository {
            return INSTANCE ?: synchronized(this) {
                Repository(apiService, productDao).also {
                    INSTANCE = it
                }
            }
        }
    }

    fun getProduct(): Flow<ResultResponse<ArrayList<Product>>> = flow {
        emit(ResultResponse.Loading)

        try {
            val product = apiService.getProducts()
            emit(ResultResponse.Success(product))
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun getDetailProduct(id: Int): Flow<ResultResponse<Product>> = flow {
        emit(ResultResponse.Loading)

        try {
            val product = apiService.getDetail(id)
            emit(ResultResponse.Success(product))
        } catch (e: Exception) {
            Log.d(TAG, e.message.toString())
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun isCheckout(id: Int): Flow<Boolean> {
        return productDao.checkoutProduct(id)
    }

    fun getCheckout(): Flow<List<Product>> {
        return productDao.getProduct()
    }

    suspend fun saveCheckout(product: Product) {
        productDao.insert(product)
    }

    suspend fun delete(product: Product) {
        productDao.deleteProduct(product)
    }
}