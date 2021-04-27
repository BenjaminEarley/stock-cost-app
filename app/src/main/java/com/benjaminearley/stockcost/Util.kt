package com.benjaminearley.stockcost

import android.content.Context
import android.content.res.Resources
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
import java.math.BigDecimal
import java.math.RoundingMode
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

infix fun Price.diff(other: Price): Double {
    val a = amount.toDouble()
    val b = other.amount.toDouble()
    return ((b - a) * 100.0) / a
}

fun Int.dpToPxInt(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Double.formatPercent(): String {
    val value = BigDecimal(this).setScale(2, RoundingMode.HALF_UP)
    val leadingChar = if (value.signum() != -1) "+" else ""
    return "$leadingChar$value%"
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