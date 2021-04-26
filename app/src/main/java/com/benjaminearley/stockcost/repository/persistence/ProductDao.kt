package com.benjaminearley.stockcost.repository.persistence

import androidx.room.*
import com.benjaminearley.stockcost.data.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE security_id=:securityId")
    fun getProduct(securityId: String): Flow<Product?>

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateProducts(product: List<Product>)

    @Delete
    fun deleteProduct(product: Product)

    @Query("DELETE FROM products")
    fun deleteAllProducts()
}

