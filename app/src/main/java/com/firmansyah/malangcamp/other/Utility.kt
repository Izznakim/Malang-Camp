package com.firmansyah.malangcamp.other

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
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

    return sum.toDouble() / ratingArray.size.toDouble()
}

fun currencyIdrFormat(): NumberFormat {
    val currencyFormat = NumberFormat.getCurrencyInstance()
    currencyFormat.maximumFractionDigits = 0
    currencyFormat.currency = Currency.getInstance("IDR")
    return currencyFormat
}

fun toBookingDetail(
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

fun copyPhoneNumber(activity: FragmentActivity?, pembayaran: Pembayaran?) {
    val clipboard =
        activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("phone", "${pembayaran?.noTelp}")
    clipboard.setPrimaryClip(clip)
    Toast.makeText(
        activity,
        "Nomor Telpon telah disalin",
        Toast.LENGTH_LONG
    )
        .show()
}