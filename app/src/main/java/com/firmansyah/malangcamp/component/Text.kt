package com.firmansyah.malangcamp.component

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.copyPhoneNumber
import com.firmansyah.malangcamp.screen.pelanggan.PelangganLoginActivity
import com.firmansyah.malangcamp.screen.pelanggan.PelangganRegisterActivity
import com.firmansyah.malangcamp.theme.darkGreen
import com.firmansyah.malangcamp.theme.green

@Composable
fun TextLoginRegister(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
        color = green, textAlign = TextAlign.Center
    )
}

@Composable
fun ListBarangTitle() {
    Text(
        text = stringResource(id = R.string.list_barang),
        modifier = Modifier.padding(top = 16.dp),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun TglSerahTerima(pembayaran: Pembayaran) {
    Text(text = buildAnnotatedString {
        append(stringResource(R.string.barang_diambil_tanggal_))
        withStyle(style = SpanStyle(color = Color.Green)) {
            append(pembayaran.tanggalPengambilan)
        }
        append(stringResource(R.string.__dan_dikembalikan_tanggal__))
        withStyle(style = SpanStyle(color = Color.Red)) {
            append(pembayaran.tanggalPengembalian)
        }
        append(stringResource(R.string.___pada_jam_))
        withStyle(style = SpanStyle(color = Color.Blue)) {
            append(pembayaran.jamPengambilan)
        }
    }, modifier = Modifier.padding(top = 8.dp))
}

@Composable
fun Hari(pembayaran: Pembayaran) {
    Text(
        text = stringResource(R.string.Selama__Hari, pembayaran.hari.toString()),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End
    )
}

@Composable
fun Total(textTotal: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.total),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = textTotal,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun NamaPenyewa(pembayaran: Pembayaran) {
    Text(
        text = stringResource(id = R.string.text_nama_penyewa),
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 16.dp)
    )
    Text(text = pembayaran.namaPenyewa)
}

@Composable
fun PhoneNumber(context: Context, pembayaran: Pembayaran) {
    Text(
        text = stringResource(id = R.string.nomor_telepon),
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        text = pembayaran.noTelp,
        color = Color.Blue,
        modifier = Modifier.clickable {
            copyPhoneNumber(context, pembayaran)
        })
}

@Composable
fun ErrorText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colors.error,
        style = MaterialTheme.typography.caption,
        softWrap = true
    )
}

@Composable
fun ErrorFailComponent(
    icons: ImageVector,
    contentDesc: String,
    textFail: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icons,
            contentDescription = contentDesc,
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = textFail,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TextDaftarSekarang(context: Context) {
    Text(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.belum_memiliki_akun))
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(stringResource(R.string.daftar_sekarang))
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, end = 24.dp)
            .clickable {
                Intent(context, PelangganRegisterActivity::class.java).also {
                    context.startActivity(it)
                }
            },
        color = darkGreen, textAlign = TextAlign.End, style = MaterialTheme.typography.caption
    )
}

@Composable
fun TextToLogin(context: Context) {
    Text(
        text = stringResource(id = R.string.sudah_memiliki_akun),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable {
                Intent(
                    context,
                    PelangganLoginActivity::class.java
                ).also {
                    context.startActivity(it)
                }
            },
        color = darkGreen,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline,
    )
}