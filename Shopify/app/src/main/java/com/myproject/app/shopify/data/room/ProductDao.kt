package com.myproject.app.shopify.data.room

import androidx.room.*
import com.myproject.app.shopify.data.response.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

    @Query("SELECT * FROM product ORDER BY id ASC")
    fun getProduct() : Flow<List<Product>>

    @Query("SELECT EXISTS(SELECT * FROM product WHERE id = :id AND favorite = 1)")
    fun checkoutProduct(id: Int): Flow<Boolean>

    @Delete
    suspend fun deleteProduct(productModel: Product)
}