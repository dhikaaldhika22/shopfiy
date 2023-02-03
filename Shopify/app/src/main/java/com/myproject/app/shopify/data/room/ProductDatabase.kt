package com.myproject.app.shopify.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myproject.app.shopify.data.response.Product

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ProductDatabase {
            if (INSTANCE == null) {
                synchronized(ProductDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, ProductDatabase::class.java, "favorite"
                    )
                        .build()
                }
            }
            return INSTANCE as ProductDatabase
        }
    }
}