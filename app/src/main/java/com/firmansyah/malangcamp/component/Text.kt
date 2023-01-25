package com.firmansyah.malangcamp.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.copyPhoneNumber
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.theme.green

@Composable
fun LoginAdmin() {
    Text(
        text = stringResource(id = R.string.login_admin),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp),
        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
        color = green, textAlign = TextAlign.Center
    )
}

@Composable
fun PhoneNumber(activity: FragmentActivity?, pembayaran: Pembayaran?) {
    Text(
        text = stringResource(id = R.string.nomor_telepon),
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        text = "${pembayaran?.noTelp}",
        color = Color.Blue,
        modifier = Modifier.clickable {
            copyPhoneNumber(activity, pembayaran)
        })
}

@Composable
fun NamaPenyewa(pembayaran: Pembayaran?) {
    Text(
        text = stringResource(id = R.string.text_nama_penyewa),
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 16.dp)
    )
    Text(text = "${pembayaran?.namaPenyewa}")
}

@Composable
fun Total(pembayaran: Pembayaran?) {
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
            text = currencyIdrFormat().format(pembayaran?.total),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Hari(pembayaran: Pembayaran?) {
    Text(
        text = "Selama ${pembayaran?.hari.toString()} Hari",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End
    )
}

@Composable
fun SerahTerima(pembayaran: Pembayaran?) {
    Text(text = buildAnnotatedString {
        append("Barang diambil tanggal: ")
        withStyle(style = SpanStyle(color = Color.Green)) {
            append("${pembayaran?.tanggalPengambilan}")
        }
        append(" dan dikembalikan tanggal: ")
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("${pembayaran?.tanggalPengembalian}")
        }
        append(", pada jam ")
        withStyle(style = SpanStyle(color = Color.Blue)) {
            append("${pembayaran?.jamPengambilan}")
        }
    }, modifier = Modifier.padding(top = 8.dp))
}

@Composable
fun ListBarangTitle() {
    Text(
        text = stringResource(id = R.string.list_barang),
        modifier = Modifier.padding(top = 16.dp),
        fontWeight = FontWeight.Bold
    )
}