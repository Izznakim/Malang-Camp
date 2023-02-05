package com.firmansyah.malangcamp.screen

import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.other.ConstVariable
import com.firmansyah.malangcamp.other.ConstVariable.Companion.ADD_BARANG_SCREEN_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG_DETAIL_SCREEN_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG_SEWA_DETAIL_SCREEN_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BOOKING_DETAIL_SCREEN_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.LIST_BARANG_SCREEN_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.LIST_BOOKING_SCREEN_ROUTE

sealed class BotNavItem(var title: Int, var icon: Int, var route: String) {
    object ListBookingScreen :
        BotNavItem(R.string.list_booking, R.drawable.note, LIST_BOOKING_SCREEN_ROUTE)

    object ListBarangScreen :
        BotNavItem(R.string.list_barang, R.drawable.hiker, LIST_BARANG_SCREEN_ROUTE)

    object ListBarangSewaScreen :
        BotNavItem(
            R.string.barang_sewa,
            R.drawable.trekking,
            ConstVariable.LIST_BARANG_SEWA_SCREEN_ROUTE
        )

    object PembayaranScreen : BotNavItem(
        R.string.pembayaran, R.drawable.buy,
        ConstVariable.PEMBAYARAN_SCREEN_ROUTE
    )

    object RiwayatPemesananScreen :
        BotNavItem(R.string.riwayat, R.drawable.note, ConstVariable.RIWAYAT_PEMESANAN_SCREEN_ROUTE)
}

sealed class Screen(val route: String) {
    object BookingDetailScreen : Screen(BOOKING_DETAIL_SCREEN_ROUTE)
    object AddBarangScreen : Screen(ADD_BARANG_SCREEN_ROUTE)
    object BarangDetailScreen : Screen(BARANG_DETAIL_SCREEN_ROUTE)
    object BarangSewaDetailScreen : Screen(BARANG_SEWA_DETAIL_SCREEN_ROUTE)
}