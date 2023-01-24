package com.firmansyah.malangcamp.component

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.toDetailPembayaran


@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.bookingItem(listPembayaran: List<Pembayaran>, context: Context) {
    items(items = listPembayaran, itemContent = {
        Card(
            onClick = { toDetailPembayaran(context, it) },
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