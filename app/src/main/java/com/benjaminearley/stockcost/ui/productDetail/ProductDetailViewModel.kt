package com.benjaminearley.stockcost.ui.productDetail

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.lifecycle.*
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.benjaminearley.stockcost.R
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.diff
import com.benjaminearley.stockcost.formatMoney
import com.benjaminearley.stockcost.formatPercent
import com.benjaminearley.stockcost.repository.ProductsRepository
import com.benjaminearley.stockcost.repository.network.LivePriceService
import com.benjaminearley.stockcost.ui.productDetail.State.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.time.milliseconds

class ProductDetailViewModel @AssistedInject constructor(
    private val repository: ProductsRepository,
    private val livePriceService: LivePriceService,
    @Assisted private val args: ProductDetailFragmentArgs
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(Loading())

    private val liveAmount: SharedFlow<String> = callbackFlow {
        withContext(Dispatchers.IO) {
            livePriceService.getProductPrice(args.securityId) { sendBlocking(it) }
        }
        awaitClose { }
    }.shareIn(viewModelScope, SharingStarted.Lazily)

    private val currentPrice: Flow<String> = merge(
        _state.mapNotNull { it.data?.product?.currentPrice?.amount },
        liveAmount
    )

    private val errorMessageChannel: BroadcastChannel<Int?> = BroadcastChannel(1)
    val errorMessages: Flow<Int?> =
        merge(
            errorMessageChannel.asFlow(),
            _state.filterIsInstance<Failure>().map { it.error }
        )

    val isLoading: LiveData<Boolean> =
        _state.map { it is Loading }.debounce(300.milliseconds).asLiveData()

    val data: LiveData<ViewData?> = combine(
        _state,
        currentPrice
    ) { state, livePrice ->
        state.data?.product?.run {
            val currentPrice = currentPrice.copy(amount = livePrice)
            val diff = closingPrice diff currentPrice
            ViewData(
                title = displayName,
                subtitle = "$symbol | ${securityId.toUpperCase(Locale.getDefault())}",
                previousDayPrice = closingPrice.formatMoney(),
                currentPrice = currentPrice.formatMoney(),
                diff = diff.formatPercent(),
                ticker = diff.getTicker()
            )
        }
    }
        .catch { errorMessageChannel.offer(R.string.error_update_product) }
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
    val currentPrice: String,
    val diff: String,
    val ticker: ViewTicker
)

enum class ViewTicker(@DrawableRes val icon: Int? = null, @AttrRes val tint: Int? = null) {
    Up(
        R.drawable.ic_up_24,
        R.attr.colorUp
    ),
    Down(
        R.drawable.ic_down_24,
        R.attr.colorDown
    ),
    None
}

private fun Double.getTicker() = when {
    this > 0 -> ViewTicker.Up
    this < 0 -> ViewTicker.Down
    else -> ViewTicker.None
}

private data class StateData(
    val product: Product
)

private sealed class State {
    abstract val data: StateData?

    data class Loading(override val data: StateData? = null) : State()
    data class Success(override val data: StateData) : State()
    data class Failure(val error: Int, override val data: StateData? = null) : State()
}

@AssistedFactory
interface ProductDetailViewModelFactory {
    fun create(args: ProductDetailFragmentArgs): ProductDetailViewModel
}