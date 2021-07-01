/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.joda.money.format.MoneyFormatter
import org.joda.money.format.MoneyFormatterBuilder

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
