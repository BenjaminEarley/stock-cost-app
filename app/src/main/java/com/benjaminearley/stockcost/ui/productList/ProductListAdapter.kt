package com.benjaminearley.stockcost.ui.productList

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.databinding.ItemProductBinding
import com.benjaminearley.stockcost.dpToPxInt


class ProductListAdapter(
    private val onClick: (Product) -> Unit
) : ListAdapter<Product, ProductListAdapter.ProductListViewHolder>(ProductDiffCallback()) {

    class ProductListViewHolder(
        private val binding: ItemProductBinding,
        private val onClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var productId: String? = null
        fun bind(Product: Product) = binding.apply {
            productId = Product.securityId
            name.text = Product.displayName
            identifier.text = Product.securityId

            root.setOnClickListener {
                onClick(Product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.securityId == newItem.securityId
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}

class SpacingItemDecoration(
    @Dimension(unit = Dimension.DP) private val dp: Int
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val px = dp.dpToPxInt()
        if (px <= 0) return
        if (parent.getChildLayoutPosition(view) >= 1) {
            if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
                outRect.top = px
            } else {
                outRect.left = px
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int =
        (parent.layoutManager as? LinearLayoutManager)
            ?.orientation
            ?: run {
                throw IllegalStateException(
                    "SpacingItemDecoration can only be used with a LinearLayoutManager."
                )
            }
}