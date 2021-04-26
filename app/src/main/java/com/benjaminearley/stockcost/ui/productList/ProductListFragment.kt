package com.benjaminearley.stockcost.ui.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.benjaminearley.stockcost.databinding.FragmentProductListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private val viewModel by viewModels<ProductListViewModel>()

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val adapter = ProductListAdapter(
        onClick = { product ->
            findNavController().navigate(
                ProductListFragmentDirections.openProductDetail(product, product.displayName)
            )
        },
        onDelete = { product ->
            TODO()
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        with(binding) {

            viewModel.products.observe(viewLifecycleOwner) { products ->
                adapter.submitList(products)
            }

            recyclerView.addItemDecoration(SpacingItemDecoration(dp = 8))
            recyclerView.adapter = adapter

            add.setOnClickListener {
                viewModel.addProduct()
            }


            return root
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}