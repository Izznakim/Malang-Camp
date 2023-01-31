package com.firmansyah.malangcamp.admin.ui.listbooking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class BookingDetailViewModel : ViewModel() {
    private val pembayaranRef = Firebase.database.getReference("pembayaran")
    private val barangRef = Firebase.database.getReference("barang")

    var pembayaran by mutableStateOf(Pembayaran())
        private set

    fun getPembayaran(mPembayaran: Pembayaran?) {
        if (mPembayaran != null) {
            pembayaran = mPembayaran
        }
    }

    fun pembayaranDitolak(idPembayaran: String, barangSewa: ArrayList<Keranjang>?) {
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
//                        Toast.makeText(context, it.message, Toast.LENGTH_LONG)
//                            .show()
                    }
            }
        }
    }

    fun pembayaranDiterima(idPembayaran: String) {
        pembayaranRef.child(idPembayaran).child("status").setValue("diterima")
    }
}