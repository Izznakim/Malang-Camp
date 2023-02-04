package com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Pembayaran

class RiwayatDetailViewModel : ViewModel() {
    var pembayaran by mutableStateOf(Pembayaran())
        private set

    fun getPembayaran(mPembayaran: Pembayaran?) {
        if (mPembayaran != null) {
            pembayaran = mPembayaran
        }
    }

}