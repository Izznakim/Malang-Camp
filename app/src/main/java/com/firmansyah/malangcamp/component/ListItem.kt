package com.firmansyah.malangcamp.component

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable.Companion.EXTRA_PEMBAYARAN
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PEMBAYARAN
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan.RiwayatDetailFragment
import com.firmansyah.malangcamp.screen.Screen
import com.firmansyah.malangcamp.theme.black
import com.google.firebase.database.DatabaseReference


@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.bookingItem(
    listPembayaran: List<Pembayaran>,
    navController: NavHostController,
    context: Context,
    pegawai: Boolean
) {
    items(items = listPembayaran, itemContent = {
        Card(
            onClick = {
                if (pegawai) {
                    navController.currentBackStackEntry?.savedStateHandle?.set(PEMBAYARAN, it)
                    navController.navigate(Screen.BookingDetailScreen.route)
                } else {
                    val riwayatDetailFragment = RiwayatDetailFragment()
                    val mFragmentManager = (context as AppCompatActivity).supportFragmentManager
                    val bundle = Bundle()

                    bundle.putParcelable(EXTRA_PEMBAYARAN, it)
                    riwayatDetailFragment.arguments = bundle
                    riwayatDetailFragment.show(
                        mFragmentManager,
                        RiwayatDetailFragment::class.java.simpleName
                    )
                }
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
                    text = context.getString(
                        R.string.Diambil______,
                        it.tanggalPengambilan,
                        it.jamPengambilan
                    ),
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

fun LazyListScope.listKeranjang(
    listKeranjang: ArrayList<Keranjang>,
    pembayaran: Boolean = false,
    keranjangRef: DatabaseReference? = null,
    context: Context? = null
) {
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
                if (pembayaran) {
                    IconButton(onClick = {
                        keranjangRef?.child(barang.idBarang)?.get()?.addOnSuccessListener {
                            it.ref.removeValue()
                        }?.addOnFailureListener {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete, contentDescription = stringResource(
                                id = R.string.tombol_hapus
                            )
                        )
                    }
                }
            }
            Divider(
                thickness = 1.dp,
                color = black
            )
        }
    })
}