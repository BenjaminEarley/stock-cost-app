package com.benjaminearley.stockcost.ui.productAdd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.benjaminearley.stockcost.R
import com.benjaminearley.stockcost.repository.ProductsRepository
import com.benjaminearley.stockcost.repository.RepoError.DatabaseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductAddViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val dismissDialogChannel: BroadcastChannel<Unit> = BroadcastChannel(1)
    val dismissDialog: Flow<Unit> = dismissDialogChannel.asFlow()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error: MutableLiveData<Int?> = MutableLiveData(null)
    val error: LiveData<Int?> = _error

    fun add(text: String?) {
        val input = text?.trim()

        if (input.isNullOrBlank()) {
            _error.value = R.string.enter_security_id
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.addNewProduct(input)) {
                is Left -> {
                    _error.value = when (result.value) {
                        DatabaseError -> R.string.product_already_added
                        else -> R.string.error_adding_product
                    }
                    _isLoading.value = false
                }
                is Right -> {
                    dismissDialogChannel.offer(Unit)
                }
            }
        }
    }

}