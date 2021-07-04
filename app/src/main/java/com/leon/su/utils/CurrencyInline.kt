package com.leon.su.utils

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.joda.money.format.MoneyFormatter
import org.joda.money.format.MoneyFormatterBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

fun getRupiahFormatter(): MoneyFormatter = MoneyFormatterBuilder()
    .appendCurrencySymbolLocalized()
    .appendAmountLocalized()
    .toFormatter(Locale("id", "ID"))

fun BigDecimal.toMoney(currencyCode: String = "IDR"): Money? {
    val currencyUnit = CurrencyUnit.of(currencyCode)
    val converted = this.setScale(currencyUnit.decimalPlaces, RoundingMode.HALF_EVEN)
    return Money.of(CurrencyUnit.of(currencyCode), converted)
}

fun String?.toDoubleCheck(): Double {
    return if (this.isNullOrBlank()) 0.0
    else {
        var temp = this
        temp = temp.replace("Rp", "", true)
        temp = temp.replace(".", "")
        temp = temp.replace(",", ".")
        temp.toDoubleOrNull() ?: 0.0
    }
}

fun BigDecimal.toRupiah(): String =
    toMoney()?.toRupiah() ?: "Rp0,00"

fun Double.toMoney(currencyCode: String = "IDR"): Money? =
    BigDecimal.valueOf(this).toMoney(currencyCode)

fun Double.toRupiah(): String =
    toMoney()?.toRupiah() ?: "Rp0,00"

fun String?.toRupiahMoney(): Money? =
    toDoubleCheck().toMoney()

fun Money.toRupiah(): String =
    getRupiahFormatter().print(this)
