package com.firmansyah.malangcamp.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.admin.Screen
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.theme.black


@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.bookingItem(listPembayaran: List<Pembayaran>, navController: NavHostController) {
    items(items = listPembayaran, itemContent = {
        Card(
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("pembayaran", it)
                navController.navigate(Screen.BookingDetailScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Diambil ${it.tanggalPengambilan}; ${it.jamPengambilan}",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = it.namaPenyewa, fontWeight = FontWeight.Bold
                )
                Text(
                    text = it.noTelp, modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    })
}

fun LazyListScope.listKeranjang(listKeranjang: ArrayList<Keranjang>) {
    items(items = listKeranjang, itemContent = { barang ->
        Column(
            modifier = Modifier
                .padding(bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = barang.namaBarang,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1.1f, true)
                        .fillMaxWidth()
                )
                Text(
                    text = currencyIdrFormat().format(barang.hargaBarang),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .border(
                            BorderStroke(
                                1.dp,
                                Color.Black
                            )
                        )
                        .weight(1f, true)
                        .fillMaxWidth()
                )
                Text(
                    text = "(${barang.jumlah})",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(0.5f, true)
                        .fillMaxWidth()
                )
                Text(
                    text = currencyIdrFormat().format(barang.hargaBarang * barang.jumlah),
                    modifier = Modifier
                        .border(
                            BorderStroke(
                                1.dp,
                                Color.Black
                            )
                        )
                        .weight(1f, true)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                thickness = 1.dp,
                color = black
            )
        }
    })
}