package br.com.andersonsoares.utils

import java.text.NumberFormat
import java.util.*

/**
 * Created by andersonsoares on 23/01/2018.
 */


fun Float.toCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this)
}