package com.firmansyah.malangcamp.screen

import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.other.ConstVariable.Companion.ADD_BARANG_SCREEN_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG_DETAIL_SCREEN_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BOOKING_DETAIL_SCREEN_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.LIST_BARANG_ROUTE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.LIST_BOOKING_ROUTE

sealed class BotNavItemPegawai(var title: Int, var icon: Int, var route: String) {
    object ListBooking :
        BotNavItemPegawai(R.string.list_booking, R.drawable.note, LIST_BOOKING_ROUTE)

    object ListBarang : BotNavItemPegawai(R.string.list_barang, R.drawable.hiker, LIST_BARANG_ROUTE)
}

sealed class Screen(val route: String) {
    object BookingDetailScreen : Screen(BOOKING_DETAIL_SCREEN_ROUTE)
    object AddBarangScreen : Screen(ADD_BARANG_SCREEN_ROUTE)
    object BarangDetailScreen : Screen(BARANG_DETAIL_SCREEN_ROUTE)
}