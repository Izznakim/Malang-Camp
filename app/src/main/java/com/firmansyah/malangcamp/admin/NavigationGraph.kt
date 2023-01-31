package com.firmansyah.malangcamp.admin

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.firmansyah.malangcamp.admin.ui.informasibarang.AddBarangScreen
import com.firmansyah.malangcamp.admin.ui.informasibarang.BarangDetailScreen
import com.firmansyah.malangcamp.admin.ui.informasibarang.ListBarangScreen
import com.firmansyah.malangcamp.admin.ui.listbooking.BookingDetailScreen
import com.firmansyah.malangcamp.admin.ui.listbooking.ListBookingScreen
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pembayaran

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BotNavItemAdmin.ListBooking.route) {
        composable(BotNavItemAdmin.ListBooking.route) {
            ListBookingScreen(navController)
        }
        composable(Screen.BookingDetailScreen.route) {
            val pembayaran =
                navController.previousBackStackEntry?.savedStateHandle?.get<Pembayaran>("pembayaran")
            BookingDetailScreen(navController, pembayaran)
        }
        composable(BotNavItemAdmin.ListBarang.route) {
            ListBarangScreen(navController)
        }
        composable(Screen.AddBarangScreen.route) {
            AddBarangScreen(navController)
        }
        composable(Screen.BarangDetailScreen.route) {
            val barang =
                navController.previousBackStackEntry?.savedStateHandle?.get<Barang>("barang")
            BarangDetailScreen(navController, barang)
        }
    }
}