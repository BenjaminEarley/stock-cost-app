package com.benjaminearley.stockcost.ui.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.benjaminearley.stockcost.databinding.FragmentProductListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        with(binding) {

            button.setOnClickListener {
                findNavController().navigate(ProductListFragmentDirections.openProductDetail())
            }

            return root
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}