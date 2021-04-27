package com.benjaminearley.stockcost.ui.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.benjaminearley.stockcost.databinding.FragmentProductListBinding
import com.benjaminearley.stockcost.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private val viewModel by viewModels<ProductListViewModel>()

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val adapter = ProductListAdapter(
        onClick = { product ->
            findNavController().navigate(
                ProductListFragmentDirections.openProductDetail(product.securityId)
            )
        }
    )

    private val deleteItemCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                (viewHolder as? ProductListAdapter.ProductListViewHolder)?.productId?.let {
                    viewModel.deleteProduct(it)
                }
            }
        }

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
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        with(binding) {

            viewModel.products.observe(viewLifecycleOwner) { products ->
                adapter.submitList(products)
            }

            recyclerView.addItemDecoration(SpacingItemDecoration(dp = 8))
            val itemTouchHelper = ItemTouchHelper(deleteItemCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
            recyclerView.adapter = adapter

            add.setOnClickListener {
                findNavController().navigate(ProductListFragmentDirections.addProduct())
            }

            return root
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}