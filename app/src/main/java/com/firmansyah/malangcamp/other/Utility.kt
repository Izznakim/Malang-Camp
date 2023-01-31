package com.firmansyah.malangcamp.other

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pembayaran
import java.text.NumberFormat
import java.util.*

fun rating(barang: Barang): Double {
    val ratingArray = barang.rating.split(", ").toTypedArray()
    var sum = 0
    if (barang.rating.isEmpty()) {
        sum = 0
    } else {
        ratingArray.forEach {
            sum += it.toInt()
        }
    }

    return sum.toDouble() / ratingArray.size.toDouble()
}

fun currencyIdrFormat(): NumberFormat {
    val currencyFormat = NumberFormat.getCurrencyInstance()
    currencyFormat.maximumFractionDigits = 0
    currencyFormat.currency = Currency.getInstance("IDR")
    return currencyFormat
}

fun copyPhoneNumber(context: Context, pembayaran: Pembayaran?) {
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("phone", "${pembayaran?.noTelp}")
    clipboard.setPrimaryClip(clip)
    Toast.makeText(
        context,
        "Nomor Telpon telah disalin",
        Toast.LENGTH_LONG
    )
        .show()
}