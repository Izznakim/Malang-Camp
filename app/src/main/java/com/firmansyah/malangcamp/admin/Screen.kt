package com.firmansyah.malangcamp.admin

import com.firmansyah.malangcamp.R

sealed class BotNavItemAdmin(var title: String, var icon: Int, var route: String) {
    object ListBooking : BotNavItemAdmin("List Booking", R.drawable.note, "listBooking")
    object ListBarang : BotNavItemAdmin("List Barang", R.drawable.hiker, "listBarang")
}

sealed class Screen(val route: String) {
    object BookingDetailScreen : Screen("bookingDetailScreen")
    object AddBarangScreen : Screen("addBarangScreen")
    object BarangDetailScreen : Screen("barangDetailScreen")
}