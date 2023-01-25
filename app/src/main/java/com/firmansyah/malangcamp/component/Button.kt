package com.firmansyah.malangcamp.component

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.admin.AdminLoginActivity
import com.firmansyah.malangcamp.admin.AdminLoginViewModel
import com.firmansyah.malangcamp.admin.ui.informasibarang.SubmitBarangFragment
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.pelanggan.PelangganLoginActivity
import com.firmansyah.malangcamp.theme.green
import com.firmansyah.malangcamp.theme.white
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue


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
fun FabAddBarang(childFragmentManager: FragmentManager) {
    FloatingActionButton(
        onClick = {
            val submitBarangFragment = SubmitBarangFragment()
            submitBarangFragment.show(
                childFragmentManager,
                SubmitBarangFragment::class.java.simpleName
            )
        }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.tombol_untuk_menambahkan_informasi_barang)
        )
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

@Composable
fun ButtonConfirm(
    idPembayaran: String,
    barangSewa: ArrayList<Keranjang>?,
    pembayaranRef: DatabaseReference,
    barangRef: DatabaseReference,
    activity: FragmentActivity?,
    dialog: Dialog?
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
                pembayaranRef.child(idPembayaran).child("status").setValue("ditolak")
                if (barangSewa?.indices != null) {
                    for (i in barangSewa.indices) {
                        barangRef.child(barangSewa[i].idBarang).child("stock").get()
                            .addOnSuccessListener {
                                val value = it.getValue<Int>()
                                if (value != null) {
                                    barangRef.child(barangSewa[i].idBarang)
                                        .child("stock")
                                        .setValue(value + barangSewa[i].jumlah)
                                }
                            }.addOnFailureListener {
                                Toast.makeText(activity, it.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                    }
                }
                Toast.makeText(activity, "Penyewaan telah diTOLAK", Toast.LENGTH_LONG)
                    .show()
                dialog?.dismiss()
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
                pembayaranRef.child(idPembayaran).child("status").setValue("diterima")
                Toast.makeText(activity, "Penyewaan telah diTERIMA", Toast.LENGTH_LONG)
                    .show()
                dialog?.dismiss()
            },
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .wrapContentSize(), colors = ButtonDefaults.buttonColors(Color.Green)
        ) {
            Text(text = stringResource(id = R.string.terima), fontWeight = FontWeight.Bold)
        }
    }
}