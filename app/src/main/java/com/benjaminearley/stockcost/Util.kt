package com.benjaminearley.stockcost

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import com.benjaminearley.stockcost.data.Price
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.*

fun Fragment.showSnackbar(@StringRes messageRes: Int?) {
    val message = messageRes?.let { getString(it) } ?: return
    val coordinator = requireActivity().findViewById<View>(R.id.main) as CoordinatorLayout
    Snackbar.make(coordinator, message, Snackbar.LENGTH_SHORT).show()
}

fun Price.formatMoney(): String = NumberFormat.getCurrencyInstance().run {
    currency = Currency.getInstance(this@formatMoney.currency)
    minimumFractionDigits = decimals
    maximumFractionDigits = decimals
    format(amount.toDouble())
}

@ColorInt
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}