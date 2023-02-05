package com.firmansyah.malangcamp.screen.pelanggan.ui.riwayatpemesanan

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BUKTI_LOCATION
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DITERIMA
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DITOLAK
import com.firmansyah.malangcamp.other.ConstVariable.Companion.NETRAL
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PEMBAYARAN
import com.firmansyah.malangcamp.other.ConstVariable.Companion.STOCK_PATH
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.firmansyah.malangcamp.theme.black
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun RiwayatDetailScreen(
    navController: NavHostController,
    pembayaran: Pembayaran?,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    viewModel: RiwayatDetailViewModel = viewModel()
) {
    viewModel.getPembayaran(pembayaran)
    val context = LocalContext.current
    val mPembayaran = viewModel.pembayaran
    val hapusText = when (mPembayaran.status) {
        NETRAL -> stringResource(R.string.batalkan_pemesanan)
        else -> stringResource(R.string.hapus_pesanan)
    }
    val barangSewa = mPembayaran.barangSewa
    val listKeranjang: ArrayList<Keranjang> = arrayListOf()
    var showDialog by remember { mutableStateOf(false) }
    val barangRef = Firebase.database.getReference(BARANG)
    val pembayaranRef = Firebase.database.getReference(PEMBAYARAN)
    val storageBuktiRef = Firebase.storage.getReference(BUKTI_LOCATION)

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
        Box {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
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
                item { Total(currencyIdrFormat().format(mPembayaran.total)) }
                item { NamaPenyewa(mPembayaran) }
                item { PhoneNumber(context, mPembayaran) }
                item { BuktiPembayaranImage(mPembayaran, context) }
                item {
                    Text(
                        text = when (mPembayaran.status) {
                            DITERIMA -> context.getString(R.string.pesanan_anda_di_terima)
                                .uppercase(
                                    Locale.getDefault()
                                )
                            DITOLAK -> context.getString(R.string.pesanan_anda_tidak_bisa_kami_proses)
                                .uppercase(
                                    Locale.getDefault()
                                )
                            else -> context.getString(R.string.pesanan_anda_belum_kami_konfirmasi)
                                .uppercase(
                                    Locale.getDefault()
                                )
                        },
                        color = when (mPembayaran.status) {
                            DITERIMA -> Color.Green
                            DITOLAK -> Color.Red
                            else -> Color.Unspecified
                        },
                        fontWeight = when (mPembayaran.status) {
                            DITERIMA -> FontWeight.Bold
                            DITOLAK -> FontWeight.Bold
                            else -> null
                        },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                item {
                    Text(text = stringResource(id = R.string.NmerTlpnPegawai),
                        color = Color.Blue,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .clickable {
                                intentToWhatsApp(
                                    context.getString(R.string.NmerTlpnPegawai),
                                    context
                                )
                            })
                }
                item {
                    Button(
                        onClick = {
                            pembayaranRef.child(mPembayaran.idPembayaran).get()
                                .addOnSuccessListener {
                                    when (mPembayaran.status) {
                                        NETRAL -> {
                                            for (i in mPembayaran.barangSewa.indices) {
                                                barangRef.child(mPembayaran.barangSewa[i].idBarang)
                                                    .child(STOCK_PATH).get()
                                                    .addOnSuccessListener { snapshot ->
                                                        val value = snapshot.getValue<Int>()
                                                        if (value != null) {
                                                            barangRef.child(mPembayaran.barangSewa[i].idBarang)
                                                                .child(STOCK_PATH)
                                                                .setValue(value + mPembayaran.barangSewa[i].jumlah)
                                                        }
                                                    }.addOnFailureListener { e ->
                                                        coroutineScope.launch {
                                                            scaffoldState.snackbarHostState.showSnackbar(
                                                                e.message.toString()
                                                            )
                                                        }
                                                    }
                                            }
                                            navController.popBackStack()
                                        }
                                        DITERIMA -> {
                                            showDialog = true
                                        }
                                        DITOLAK -> {
                                            navController.popBackStack()
                                        }
                                    }

                                    it.ref.removeValue()
                                    storageBuktiRef.child("${mPembayaran.idPembayaran}.jpg")
                                        .delete()
                                }.addOnFailureListener {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(it.message.toString())
                                    }
                                }
                        },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = hapusText, textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (showDialog) {
                val list by remember { mutableStateOf(listOf(1, 2, 3, 4, 5)) }
                var rating by rememberSaveable { mutableStateOf(0) }

                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                        navController.popBackStack()
                    },
                    title =
                    {
                        Text(
                            text = context.getString(R.string.beri_nilai_pada_barang_barang_yang_sudah_kamu_sewa),
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                list.forEach {
                                    Row {
                                        RadioButton(
                                            selected = rating == it,
                                            onClick = { rating = it }
                                        )
                                        Text(text = it.toString(), modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .clickable {
                                                rating = it
                                            })
                                    }
                                }
                            }
                            Text(
                                text = when (rating) {
                                    1 -> context.getString(R.string.sangat_kurang)
                                    2 -> context.getString(R.string.kurang)
                                    3 -> context.getString(R.string.cukup)
                                    4 -> context.getString(R.string.baik)
                                    5 -> context.getString(R.string.sangat_baik)
                                    else -> context.getString(R.string.belum_diberi_rating)
                                }, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.addRating(barangSewa, rating)
                                showDialog = false
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                        ) {
                            Text(
                                text = stringResource(id = R.string.kirim),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }, dismissButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                        ) {
                            Text(
                                text = stringResource(R.string.tidak_terima_kasih),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                )
            }
        }
    }
}

private fun intentToWhatsApp(phone: String, context: Context) {
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_VIEW)

    if (packageManager != null) {
        try {
            val url = "https://api.whatsapp.com/send?phone=$phone"
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(url)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.startActivity(intent)
            } else {
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
