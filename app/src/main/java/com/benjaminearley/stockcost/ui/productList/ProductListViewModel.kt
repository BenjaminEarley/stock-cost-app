package com.benjaminearley.stockcost.ui.productList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            repository.getSavedProducts().collect {
                _products.value = it
            }
        }
    }

    fun addProduct() {
        viewModelScope.launch {
            repository.addNewProduct("sb26500")
        }

    }
}