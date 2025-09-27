package com.syadd.myapplication_1_foodapp.Helper

import java.text.NumberFormat
import java.util.Locale

/**
 * Fungsi utilitas untuk mengonversi harga ke format Rupiah
 */
fun Double.toRupiah(): String {
    val localeID = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(localeID)
    format.minimumFractionDigits = 0   // hilangkan ,00
    format.maximumFractionDigits = 0   // tidak tampilkan desimal
    return format.format(this)
}