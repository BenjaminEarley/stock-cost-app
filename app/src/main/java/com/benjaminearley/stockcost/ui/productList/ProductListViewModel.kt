package com.benjaminearley.stockcost.ui.productList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val errorMessageChannel: BroadcastChannel<String?> = BroadcastChannel(1)
    val errorMessages: Flow<String?> = errorMessageChannel.asFlow()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val products: LiveData<List<Product>> = _products

    init {
        viewModelScope.launch {
            repository.getSavedProducts().collect { products ->
                _products.value = products.sortedBy { it.displayName }
            }
        }
    }

    fun addProduct() {
        viewModelScope.launch {

        }

    }

    fun deleteProduct(securityId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (repository.deleteProduct(securityId)) {
                is Left -> errorMessageChannel.offer("Error")
                is Right -> Unit
            }
            _isLoading.value = false
        }
    }
}