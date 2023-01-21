package com.firmansyah.malangcamp.component

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.admin.AdminLoginActivity
import com.firmansyah.malangcamp.admin.AdminLoginViewModel
import com.firmansyah.malangcamp.pelanggan.PelangganLoginActivity
import com.firmansyah.malangcamp.theme.green
import com.firmansyah.malangcamp.theme.white


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
            text = "Admin",
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
            text = "Pelanggan",
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