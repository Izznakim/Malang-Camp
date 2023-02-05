package com.firmansyah.malangcamp.screen.pelanggan.ui.riwayatpemesanan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RiwayatDetailViewModel : ViewModel() {
    private val barangRef = Firebase.database.getReference(ConstVariable.BARANG)

    var pembayaran by mutableStateOf(Pembayaran())
        private set

    fun getPembayaran(mPembayaran: Pembayaran?) {
        if (mPembayaran != null) {
            pembayaran = mPembayaran
        }
    }

    fun addRating(barangSewa: ArrayList<Keranjang>?, rating: Int) {
        val listNilai: ArrayList<Int> = arrayListOf()

        barangSewa?.forEach {
            barangRef.child(it.idBarang).child(ConstVariable.RATING_PATH).get()
                .addOnSuccessListener { data ->
                    listNilai.clear()
                    listNilai.add(rating)

                    val result = data.value
                    result?.toString()?.split(", ")?.toTypedArray()
                        ?.forEach { item ->
                            listNilai.add(item.toInt())
                        }
                    barangRef.child(it.idBarang).child(ConstVariable.RATING_PATH).setValue(
                        listNilai.joinToString { item -> item.toString() }
                    )
                }
        }
    }

}