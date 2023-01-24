package com.firmansyah.malangcamp.other

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firmansyah.malangcamp.admin.ui.informasibarang.DetailInformasiFragment
import com.firmansyah.malangcamp.admin.ui.listbooking.BookingDetailFragment
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

    val rating: Double = sum.toDouble() / ratingArray.size.toDouble()
    return rating
}

fun currencyIdrFormat(): NumberFormat {
    val currencyFormat = NumberFormat.getCurrencyInstance()
    currencyFormat.maximumFractionDigits = 0
    currencyFormat.currency = Currency.getInstance("IDR")
    return currencyFormat
}

fun toDetailPembayaran(
    context: Context,
    pembayaran: Pembayaran
) {
    val bookingDetailFragment = BookingDetailFragment()
    val mFragmentManager = (context as AppCompatActivity).supportFragmentManager
    val bundle = Bundle()

    bundle.putParcelable(BookingDetailFragment.EXTRA_PEMBAYARAN, pembayaran)
    bundle.putBoolean(BookingDetailFragment.EXTRA_ISADMIN, true)
    bookingDetailFragment.arguments = bundle
    bookingDetailFragment.show(mFragmentManager, DetailInformasiFragment::class.java.simpleName)
}

fun toDetailInformasiBarang(
    barang: Barang, context: Context
) {
    val detailInformasiFragment = DetailInformasiFragment()
    val mFragmentManager =
        (context as AppCompatActivity).supportFragmentManager
    val bundle = Bundle()

    bundle.putParcelable(DetailInformasiFragment.EXTRA_BARANG, barang)
    detailInformasiFragment.show(
        mFragmentManager,
        DetailInformasiFragment::class.java.simpleName
    )
    detailInformasiFragment.arguments = bundle
}