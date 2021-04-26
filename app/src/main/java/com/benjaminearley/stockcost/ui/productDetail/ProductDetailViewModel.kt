package com.benjaminearley.stockcost.ui.productDetail

import androidx.lifecycle.*
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.benjaminearley.stockcost.R
import com.benjaminearley.stockcost.data.Price
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.ProductsRepository
import com.benjaminearley.stockcost.ui.productDetail.State.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import kotlin.time.milliseconds

class ProductDetailViewModel @AssistedInject constructor(
    private val repository: ProductsRepository,
    @Assisted private val args: ProductDetailFragmentArgs
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(Loading())

    val isLoading: LiveData<Boolean> =
        _state.map { it is Loading }.debounce(300.milliseconds).asLiveData()

    val data: LiveData<ViewData?> = _state
        .map {
            it.data?.product?.run {
                ViewData(
                    title = displayName,
                    subtitle = "$symbol | ${securityId.toUpperCase(Locale.getDefault())}",
                    previousDayPrice = closingPrice.formatMoney(),
                    currentPrice = currentPrice.formatMoney()
                )
            }
        }
        .asLiveData()

    init {
        viewModelScope.launch { load() }
    }

    fun refresh() {
        viewModelScope.launch { load(true) }
    }

    private suspend fun load(refresh: Boolean = false) {
        _state.value = Loading(_state.value.data)
        _state.value = when (val result = repository.getProduct(args.securityId, refresh)) {
            is Left ->
                Failure(R.string.error_loading_product, _state.value.data)
            is Right ->
                Success(_state.value.data?.copy(product = result.value) ?: StateData(result.value))
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            assistedFactory: ProductDetailViewModelFactory,
            args: ProductDetailFragmentArgs
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(args) as T
            }
        }
    }
}

data class ViewData(
    val title: String,
    val subtitle: String,
    val previousDayPrice: String,
    val currentPrice: String
)

private data class StateData(
    val product: Product
)

private sealed class State {
    abstract val data: StateData?

    data class Loading(override val data: StateData? = null) : State()
    data class Success(override val data: StateData) : State()
    data class Failure(val error: Int, override val data: StateData? = null) : State()
}

private fun Price.formatMoney(): String =
    NumberFormat.getCurrencyInstance().run {
        currency = Currency.getInstance(this@formatMoney.currency)
        minimumFractionDigits = decimals
        maximumFractionDigits = decimals
        format(amount.toDouble())
    }

@AssistedFactory
interface ProductDetailViewModelFactory {
    fun create(args: ProductDetailFragmentArgs): ProductDetailViewModel
}