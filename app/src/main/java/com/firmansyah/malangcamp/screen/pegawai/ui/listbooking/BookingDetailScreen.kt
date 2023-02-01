package com.firmansyah.malangcamp.screen.pegawai.ui.listbooking

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.firmansyah.malangcamp.theme.black
import kotlinx.coroutines.CoroutineScope

@Composable
fun BookingDetailScreen(
    navController: NavHostController,
    pembayaran: Pembayaran?,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    bookingDetailViewModel: BookingDetailViewModel = viewModel()
) {
    bookingDetailViewModel.getPembayaran(pembayaran)
    val mPembayaran = bookingDetailViewModel.pembayaran
    val listKeranjang: ArrayList<Keranjang> = arrayListOf()
    val context = LocalContext.current
    val idPembayaran = mPembayaran.idPembayaran
    val barangSewa = mPembayaran.barangSewa

    for (i in barangSewa.indices) {
        val keranjang = Keranjang()
        keranjang.idBarang = barangSewa[i].idBarang
        keranjang.namaBarang = barangSewa[i].namaBarang
        keranjang.hargaBarang = barangSewa[i].hargaBarang
        keranjang.jumlah = barangSewa[i].jumlah
        keranjang.subtotal = barangSewa[i].subtotal
        listKeranjang.add(keranjang)
    }
    MalangCampTheme {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            item { ListBarangTitle() }
            item { TglSerahTerima(mPembayaran) }
            item {
                Divider(
                    thickness = 1.dp,
                    color = black,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            listKeranjang(listKeranjang)
            item { Hari(mPembayaran) }
            item { Total(mPembayaran) }
            item { NamaPenyewa(mPembayaran) }
            item { PhoneNumber(context, mPembayaran) }
            item { BuktiPembayaranImage(mPembayaran, context) }
            item {
                ButtonConfirm(
                    navController,
                    idPembayaran,
                    barangSewa,
                    bookingDetailViewModel,
                    context,
                    scaffoldState,
                    coroutineScope
                )
            }
        }
    }
}