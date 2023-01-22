package com.firmansyah.malangcamp.component

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import com.firmansyah.malangcamp.admin.ui.informasibarang.DetailInformasiFragment
import com.firmansyah.malangcamp.admin.ui.listbooking.BookingDetailFragment
import com.firmansyah.malangcamp.model.Pembayaran


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


private fun toDetailPembayaran(
    context: Context,
    pembayaran: Pembayaran
) {
    val bookingDetailFragment = BookingDetailFragment()
    val mFragmentManager = (context as AppCompatActivity).supportFragmentManager
    val bundle = Bundle()

    bundle.putParcelable(BookingDetailFragment.EXTRA_PEMBAYARAN, pembayaran)
    bundle.putBoolean(BookingDetailFragment.EXTRA_ISADMIN, true)
    bookingDetailFragment.arguments = bundle
    bookingDetailFragment.show(mFragmentManager, DetailInformasiFragment::class.java.simpleName)
}