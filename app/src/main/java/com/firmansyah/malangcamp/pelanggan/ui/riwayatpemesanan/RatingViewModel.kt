package com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan

import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.other.ConstVariable
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RatingViewModel : ViewModel() {
    private val barangRef = Firebase.database.getReference(BARANG)

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