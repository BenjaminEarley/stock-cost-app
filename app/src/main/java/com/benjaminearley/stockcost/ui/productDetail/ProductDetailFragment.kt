package com.benjaminearley.stockcost.ui.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.benjaminearley.stockcost.databinding.FragmentProductDetailBinding
import com.benjaminearley.stockcost.showSnackbar
import com.benjaminearley.stockcost.themeColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val args: ProductDetailFragmentArgs by navArgs()

    @Inject
    lateinit var serviceFactory: ProductDetailViewModelFactory
    private val viewModel by viewModels<ProductDetailViewModel> {
        ProductDetailViewModel.provideFactory(serviceFactory, args)
    }

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    init {
        lifecycleScope.launchWhenStarted {
            viewModel.errorMessages.collect {
                showSnackbar(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        with(binding) {

            viewModel.isLoading.observe(viewLifecycleOwner) {
                refresh.isRefreshing = it
            }

            refresh.setOnRefreshListener {
                viewModel.refresh()
            }

            with(binding) {
                viewModel.data.observe(viewLifecycleOwner) { data ->

                    TransitionManager.beginDelayedTransition(layout, Fade())

                    if (data != null) {
                        title.text = data.title
                        subtitle.text = data.subtitle
                        previousDayPrice.text = data.previousDayPrice
                        currentPrice.text = data.currentPrice
                        diff.text = data.diff

                        if (data.ticker.icon != null && data.ticker.tint != null) {
                            val icon = ContextCompat.getDrawable(requireContext(), data.ticker.icon)
                            icon?.setTint(requireContext().themeColor(data.ticker.tint))
                            tickerIcon.setImageDrawable(icon)
                            tickerIcon.isInvisible = false
                        } else {
                            tickerIcon.isInvisible = true
                        }

                        group.isInvisible = false
                    } else {
                        group.isInvisible = true
                    }
                }
            }

            return root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}