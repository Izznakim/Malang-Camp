package com.firmansyah.malangcamp.component

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentManager
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.admin.AdminLoginActivity
import com.firmansyah.malangcamp.admin.AdminLoginViewModel
import com.firmansyah.malangcamp.admin.ui.informasibarang.SubmitBarangFragment
import com.firmansyah.malangcamp.model.Barang
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