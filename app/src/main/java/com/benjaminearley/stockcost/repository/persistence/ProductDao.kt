package com.benjaminearley.stockcost.repository.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benjaminearley.stockcost.data.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE security_id=:securityId")
    fun getProduct(securityId: String): Product

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateProducts(product: List<Product>)

    @Query("DELETE FROM products WHERE security_id = :securityId")
    fun deleteProductById(securityId: String)

    @Query("DELETE FROM products")
    fun deleteAllProducts()
}

