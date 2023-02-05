package com.firmansyah.malangcamp.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DELETE_PATH
import com.firmansyah.malangcamp.screen.Screen
import com.firmansyah.malangcamp.screen.pegawai.PegawaiLoginViewModel
import com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang.AddBarangViewModel
import com.firmansyah.malangcamp.screen.pegawai.ui.listbooking.BookingDetailViewModel
import com.firmansyah.malangcamp.screen.pelanggan.PelangganLoginViewModel
import com.firmansyah.malangcamp.screen.pelanggan.PelangganRegisterViewModel
import com.firmansyah.malangcamp.theme.green
import com.firmansyah.malangcamp.theme.white
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun ButtonHome(context: Context, intent: Intent, paddingTop: Dp, stringId: Int) {
    Button(
        onClick = {
            context.startActivity(intent)
        },
        modifier = Modifier
            .padding(top = paddingTop)
            .size(width = 246.dp, height = 50.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = green)
    ) {
        Text(
            text = stringResource(id = stringId), style = MaterialTheme.typography.h6.copy(
                fontSize = 18.sp
            )
        )
    }
}

@Composable
fun ButtonPegawaiLogin(
    viewModel: PegawaiLoginViewModel,
    email: String,
    password: String,
    emailError: Boolean,
    passwordError: Boolean,
    context: Context
) {
    Button(
        onClick = {
            val blmDftrText = context.getString(R.string.maaf_anda_belum_terdaftar_sebagai_admin)
            val failLoad = context.getString(R.string.gagal_untuk_memuat_data)
            viewModel.getPegawai(email, password, blmDftrText, failLoad)
        },
        enabled = !emailError && !passwordError,
        modifier = Modifier
            .width(235.dp)
            .padding(top = 34.dp),
        colors = ButtonDefaults.buttonColors(
            green
        )
    ) {
        Text(
            text = stringResource(id = R.string.masuk), fontWeight = FontWeight.Bold, color = white
        )
    }
    Text(
        text = viewModel.errorText.value,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.caption,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ButtonConfirm(
    navController: NavHostController,
    idPembayaran: String,
    barangSewa: ArrayList<Keranjang>,
    bookingDetailViewModel: BookingDetailViewModel,
    context: Context,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = {
                navController.popBackStack()
                coroutineScope.launch {
                    bookingDetailViewModel.pembayaranDitolak(idPembayaran, barangSewa)
                    bookingDetailViewModel.getMsgSuccess(context.getString(R.string.penyewaan_telah_ditolak))
                    bookingDetailViewModel.msg.also {
                        scaffoldState.snackbarHostState.showSnackbar(message = it)
                    }
                }
            },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .wrapContentSize(),
            border = BorderStroke(1.dp, Color.Red),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text(
                text = stringResource(id = R.string.tolak),
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = {
                navController.popBackStack()
                coroutineScope.launch {
                    bookingDetailViewModel.pembayaranDiterima(idPembayaran)
                    scaffoldState.snackbarHostState.showSnackbar(message = context.getString(R.string.penyewaan_telah_diterima))
                }
            },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(green),
        ) {
            Text(text = stringResource(id = R.string.terima), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FabAddBarang(navController: NavHostController) {
    FloatingActionButton(onClick = {
        navController.navigate(Screen.AddBarangScreen.route)
    }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.tombol_untuk_menambahkan_informasi_barang)
        )
    }
}

@Composable
fun radioButtonJnsBhnBarang(
    list: List<String>,
    deskBarang: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .horizontalScroll(
            rememberScrollState()
        )
): String {
    var deskBrng by remember { mutableStateOf(deskBarang) }
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.Center
    ) {
        list.forEach {
            Row {
                RadioButton(selected = deskBrng == it, onClick = { deskBrng = it })
                Text(text = it, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        deskBrng = it
                    })
            }
        }
    }
    return deskBrng
}

@Composable
fun SubmitBarangButton(
    imageUri: Uri?,
    jenisBarang: String,
    namaBarang: String,
    bahanBarang: String,
    tipeBarang: String,
    ukuranBarang: String,
    frameBarang: String,
    pasakBarang: String,
    warnaBarang: String,
    stockBarang: String,
    hargaBarang: String,
    caraPemasangan: String,
    kegunaanBarang: String,
    isError: Boolean,
    navController: NavHostController,
    viewModel: AddBarangViewModel,
    context: Context,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .wrapContentSize(),
            border = BorderStroke(1.dp, Color.Red),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.uploadToFirebase(
                        uri = imageUri,
                        jenisBarang,
                        namaBarang,
                        bahanBarang,
                        tipeBarang,
                        ukuranBarang,
                        frameBarang,
                        pasakBarang,
                        warnaBarang,
                        stockBarang,
                        hargaBarang,
                        caraPemasangan,
                        kegunaanBarang
                    )
                    navController.popBackStack()
                    viewModel.getMsg(context.getString(R.string.berhasil_menambahkan_barang))
                    viewModel.msg.also {
                        scaffoldState.snackbarHostState.showSnackbar(it)
                    }
                }
            },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .wrapContentSize(),
            colors = ButtonDefaults.buttonColors(
                Color.Green
            ),
            enabled = !isError
        ) {
            Text(
                text = stringResource(id = R.string.submit), fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DeleteBarangButton(barang: Barang, databaseRef: DatabaseReference) {
    OutlinedButton(
        onClick = {
            databaseRef.child(barang.id).child(DELETE_PATH).setValue(true)
        },
        modifier = Modifier.size(25.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Red),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.delete_icon),
            modifier = Modifier.size(15.dp)
        )
    }
}

@Composable
fun ButtonPelangganLogin(
    viewModel: PelangganLoginViewModel,
    email: String,
    password: String,
    emailError: Boolean,
    passwordError: Boolean,
    context: Context
) {
    Button(
        onClick = {
            val blmDftrText = context.getString(R.string.pelanggan_belum_terdaftar)
            viewModel.getPelanggan(email, password, blmDftrText)
        },
        enabled = !emailError && !passwordError,
        modifier = Modifier
            .width(235.dp)
            .padding(top = 34.dp),
        colors = ButtonDefaults.buttonColors(
            green
        )
    ) {
        Text(
            text = stringResource(id = R.string.masuk), fontWeight = FontWeight.Bold, color = white
        )
    }
    Text(
        text = viewModel.errorText.value,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.caption,
        textAlign = TextAlign.Center
    )
}


@Composable
fun ButtonPelangganRegister(
    viewModel: PelangganRegisterViewModel,
    username: String,
    email: String,
    namaDepan: String,
    namaBelakang: String,
    nomorTelepon: String,
    password: String,
    isNotError: Boolean,
    context: Context
) {
    Button(
        onClick = {
            viewModel.registerPelanggan(
                username,
                email,
                namaDepan,
                namaBelakang,
                nomorTelepon,
                password,
                context.getString(R.string.pembuatan_akun_sukses)
            )
        },
        enabled = isNotError,
        modifier = Modifier
            .width(235.dp)
            .padding(top = 32.dp),
        colors = ButtonDefaults.buttonColors(
            green
        )
    ) {
        Text(
            text = stringResource(id = R.string.daftar), fontWeight = FontWeight.Bold, color = white
        )
    }
    Text(
        text = viewModel.msgText.value,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.caption,
        textAlign = TextAlign.Center
    )
}