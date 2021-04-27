package com.benjaminearley.stockcost.ui.productDetail

import androidx.lifecycle.*
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.benjaminearley.stockcost.R
import com.benjaminearley.stockcost.data.Price
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.ProductsRepository
import com.benjaminearley.stockcost.repository.network.SubscriptionService
import com.benjaminearley.stockcost.ui.productDetail.State.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.NumberFormat
import java.util.*
import kotlin.time.milliseconds

class ProductDetailViewModel @AssistedInject constructor(
    private val repository: ProductsRepository,
    private val subscriptionService: SubscriptionService,
    @Assisted private val args: ProductDetailFragmentArgs
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(Loading())

    private val liveAmount: Flow<String> = callbackFlow {
        val result = subscriptionService.getProductPrice(args.securityId) {
            sendBlocking(it)
        }
        when (result) {
            is Left -> Timber.e(result.value)
            is Right -> Timber.d("Closed Subscription Safely")
        }
    }

    val isLoading: LiveData<Boolean> =
        _state.map { it is Loading }.debounce(300.milliseconds).asLiveData()

    val data: LiveData<ViewData?> = combine(
        _state,
        liveAmount.onStart { emit("") }
    ) { state, livePrice ->
        state.data?.product?.run {
            val amount =
                livePrice.blankToNull()?.let { currentPrice.copy(amount = it) } ?: currentPrice
            ViewData(
                title = displayName,
                subtitle = "$symbol | ${securityId.toUpperCase(Locale.getDefault())}",
                previousDayPrice = closingPrice.formatMoney(),
                currentPrice = amount.formatMoney()
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

fun String?.blankToNull(): String? = if (this.isNullOrBlank()) null else this

@AssistedFactory
interface ProductDetailViewModelFactory {
    fun create(args: ProductDetailFragmentArgs): ProductDetailViewModel
}