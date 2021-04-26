package com.benjaminearley.stockcost.ui.productList

import android.content.res.Resources
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


class ProductListAdapter(
    private val onClick: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : ListAdapter<Product, ProductListAdapter.ProductListViewHolder>(ProductDiffCallback()) {

    class ProductListViewHolder(
        private val binding: ItemProductBinding,
        private val onClick: (Product) -> Unit,
        private val onDelete: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(Product: Product) = binding.apply {
            name.text = Product.displayName
            identifier.text = Product.securityId

            root.setOnClickListener {
                onClick(Product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductListViewHolder(
        ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick,
        onDelete
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

class MarginItemDecoration(
    @Dimension(unit = Dimension.DP) private val dp: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val px = dp.dpToPxInt()
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = px
            }
            left = px
            right = px
            bottom = px
        }
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
                    "SpacingItemDecoration can only " +
                            "be used with a LinearLayoutManager."
                )
            }
}

fun Int.dpToPxInt(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}