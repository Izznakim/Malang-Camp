package com.firmansyah.malangcamp.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.admin.AdminLoginActivity
import com.firmansyah.malangcamp.admin.AdminLoginViewModel
import com.firmansyah.malangcamp.admin.Screen
import com.firmansyah.malangcamp.admin.ui.informasibarang.AddBarangViewModel
import com.firmansyah.malangcamp.admin.ui.listbooking.BookingDetailViewModel
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.pelanggan.PelangganLoginActivity
import com.firmansyah.malangcamp.theme.green
import com.firmansyah.malangcamp.theme.white
import com.google.firebase.database.DatabaseReference


@Composable
fun ButtonAdmin(context: Context) {
    Button(
        onClick = {
            Intent(context, AdminLoginActivity::class.java).also {
                context.startActivity(it)
            }
        },
        modifier = Modifier
            .padding(top = 56.dp)
            .size(width = 246.dp, height = 50.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = green)
    ) {
        Text(
            text = stringResource(id = R.string.admin),
            style = MaterialTheme.typography.h6.copy(
                fontSize = 18.sp
            )
        )
    }
}

@Composable
fun ButtonPelanggan(context: Context) {
    Button(
        onClick = {
            Intent(context, PelangganLoginActivity::class.java).also {
                context.startActivity(it)
            }
        },
        modifier = Modifier
            .padding(top = 20.dp)
            .size(width = 246.dp, height = 50.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = green)
    ) {
        Text(
            text = stringResource(id = R.string.pelanggan),
            style = MaterialTheme.typography.h6.copy(
                fontSize = 18.sp
            )
        )
    }
}

@Composable
fun ButtonAdminLogin(
    viewModel: AdminLoginViewModel,
    email: String,
    password: String,
    emailError: Boolean,
    passwordError: Boolean
) {
    Button(
        onClick = {
            viewModel.getAdmin(email, password)
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
            text = stringResource(id = R.string.masuk),
            fontWeight = FontWeight.Bold,
            color = white
        )
    }
    Text(
        text = viewModel.errorText.value,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.caption,
        modifier = Modifier.width(235.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun ButtonConfirm(
    navController: NavHostController,
    idPembayaran: String,
    barangSewa: ArrayList<Keranjang>,
    bookingDetailViewModel: BookingDetailViewModel,
    context: Context
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
                bookingDetailViewModel.pembayaranDitolak(idPembayaran, barangSewa)
                Toast.makeText(context, "Penyewaan telah diTOLAK", Toast.LENGTH_LONG)
                    .show()
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
                text = stringResource(id = R.string.tolak),
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            onClick = {
                bookingDetailViewModel.pembayaranDiterima(idPembayaran)
                Toast.makeText(context, "Penyewaan telah diTERIMA", Toast.LENGTH_LONG)
                    .show()
                navController.popBackStack()
            },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .wrapContentSize(), colors = ButtonDefaults.buttonColors(Color.Green)
        ) {
            Text(text = stringResource(id = R.string.terima), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FabAddBarang(navController: NavHostController) {
    FloatingActionButton(
        onClick = {
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
                RadioButton(
                    selected = deskBrng == it,
                    onClick = { deskBrng = it },
//                    colors = RadioButtonDefaults.colors(Color.Green)
                )
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
    viewModel: AddBarangViewModel
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
            },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .wrapContentSize(), colors = ButtonDefaults.buttonColors(
                Color.Green
            ),
            enabled = !isError
        ) {
            Text(
                text = stringResource(id = R.string.submit),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DeleteBarangButton(barang: Barang, databaseRef: DatabaseReference) {
    OutlinedButton(
        onClick = {
            databaseRef.child(barang.id).child("delete").setValue(true)
        },
        modifier = Modifier.size(25.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Red),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Delete Icon",
            modifier = Modifier.size(15.dp)
        )
    }
}
