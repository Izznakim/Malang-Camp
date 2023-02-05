package com.firmansyah.malangcamp.screen

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PEMBAYARAN
import com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang.AddBarangScreen
import com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang.BarangDetailScreen
import com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang.ListBarangScreen
import com.firmansyah.malangcamp.screen.pegawai.ui.listbooking.BookingDetailScreen
import com.firmansyah.malangcamp.screen.pegawai.ui.listbooking.ListBookingScreen
import com.firmansyah.malangcamp.screen.pelanggan.ui.barangsewa.BarangSewaDetailScreen
import com.firmansyah.malangcamp.screen.pelanggan.ui.barangsewa.ListBarangSewaScreen
import com.firmansyah.malangcamp.screen.pelanggan.ui.pembayaran.PembayaranScreen
import com.firmansyah.malangcamp.screen.pelanggan.ui.riwayatpemesanan.RiwayatDetailScreen
import com.firmansyah.malangcamp.screen.pelanggan.ui.riwayatpemesanan.RiwayatPemesananScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun NavigationGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    pegawai: Boolean
) {
    NavHost(
        navController,
        startDestination = if (pegawai) BotNavItem.ListBookingScreen.route else BotNavItem.ListBarangSewaScreen.route
    ) {
        composable(BotNavItem.ListBookingScreen.route) {
            ListBookingScreen(navController)
        }
        composable(Screen.BookingDetailScreen.route) {
            val pembayaran =
                navController.previousBackStackEntry?.savedStateHandle?.get<Pembayaran>(PEMBAYARAN)
            BookingDetailScreen(
                navController, pembayaran,
                scaffoldState,
                coroutineScope
            )
        }
        composable(BotNavItem.ListBarangScreen.route) {
            ListBarangScreen(
                navController,
                scaffoldState,
                coroutineScope
            )
        }
        composable(Screen.AddBarangScreen.route) {
            AddBarangScreen(
                navController,
                scaffoldState,
                coroutineScope
            )
        }
        composable(Screen.BarangDetailScreen.route) {
            val barang =
                navController.previousBackStackEntry?.savedStateHandle?.get<Barang>(BARANG)
            BarangDetailScreen(
                navController, barang,
                scaffoldState,
                coroutineScope
            )
        }
        composable(BotNavItem.ListBarangSewaScreen.route) {
            ListBarangSewaScreen(navController)
        }
        composable(Screen.BarangSewaDetailScreen.route) {
            val barang =
                navController.previousBackStackEntry?.savedStateHandle?.get<Barang>(BARANG)
            BarangSewaDetailScreen(
                navController, barang,
                scaffoldState,
                coroutineScope
            )
        }
        composable(BotNavItem.PembayaranScreen.route) {
            PembayaranScreen(scaffoldState, coroutineScope)
        }
        composable(BotNavItem.RiwayatPemesananScreen.route) {
            RiwayatPemesananScreen(navController)
        }
        composable(Screen.RiwayatDetailScreen.route) {
            val pembayaran =
                navController.previousBackStackEntry?.savedStateHandle?.get<Pembayaran>(PEMBAYARAN)
            RiwayatDetailScreen(
                navController, pembayaran,
                scaffoldState,
                coroutineScope
            )
        }
    }
}