package com.firmansyah.malangcamp.screen.pegawai.ui.listbooking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DITERIMA
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DITOLAK
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PEMBAYARAN
import com.firmansyah.malangcamp.other.ConstVariable.Companion.STATUS_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.STOCK_PATH
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class BookingDetailViewModel : ViewModel() {
    private val pembayaranRef = Firebase.database.getReference(PEMBAYARAN)
    private val barangRef = Firebase.database.getReference(BARANG)

    var pembayaran by mutableStateOf(Pembayaran())
        private set

    var msg by mutableStateOf("")
        private set

    fun getPembayaran(mPembayaran: Pembayaran?) {
        if (mPembayaran != null) {
            pembayaran = mPembayaran
        }
    }

    fun getMsgSuccess(message: String) {
        msg = message
    }

    fun pembayaranDitolak(idPembayaran: String, barangSewa: ArrayList<Keranjang>?) {
        if (barangSewa?.indices != null) {
            for (i in barangSewa.indices) {
                barangRef.child(barangSewa[i].idBarang).child(STOCK_PATH).get()
                    .addOnSuccessListener {
                        val value = it.getValue<Int>()
                        if (value != null) {
                            barangRef.child(barangSewa[i].idBarang)
                                .child(STOCK_PATH)
                                .setValue(value + barangSewa[i].jumlah)
                            pembayaranRef.child(idPembayaran).child(STATUS_PATH).setValue(DITOLAK)
                        }
                        msg
                    }.addOnFailureListener {
                        msg = it.message.toString()
                    }
            }
        }
    }

    fun pembayaranDiterima(idPembayaran: String) {
        pembayaranRef.child(idPembayaran).child(STATUS_PATH).setValue(DITERIMA)
    }
}