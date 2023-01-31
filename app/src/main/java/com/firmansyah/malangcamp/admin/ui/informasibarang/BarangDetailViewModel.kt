package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BarangDetailViewModel : ViewModel() {
    private val storageRef = Firebase.storage.getReference("images/")
    private val databaseRef = Firebase.database.getReference("barang")

    var barang by mutableStateOf(Barang())
        private set

    fun getBarang(mBarang: Barang?) {
        if (mBarang != null) {
            barang = mBarang
        }
    }

    fun updateBarang(
//        context: Context,
        uri: Uri?,
        it: Barang,
        newNamaBarang: String,
        newUkuranBarang: String,
        newTipeTenda: String,
        newFrameTenda: String,
        newPasakTenda: String,
        newWarnaBarang: String,
        newStockBarang: String,
        newHargaBarang: String,
        newCaraPemasangan: String,
        newKegunaanBarang: String
    ) {
        var imageUri: Uri?
        val fileRef = storageRef.child("${it.id}.jpg")
        if (uri != null) {
            fileRef.putFile(uri).addOnSuccessListener { task ->
                if (task.metadata != null && task.metadata?.reference != null) {
                    val result = task.storage.downloadUrl
                    result.addOnSuccessListener { mUri ->
                        imageUri = mUri
                        uploadToDatabase(
                            it,
                            newNamaBarang,
                            newTipeTenda,
                            newUkuranBarang,
                            newFrameTenda,
                            newPasakTenda,
                            newWarnaBarang,
                            newStockBarang,
                            newHargaBarang,
                            newCaraPemasangan,
                            newKegunaanBarang,
                            imageUri.toString(),
//                            context
                        )
                    }
                }
            }
        } else {
            uploadToDatabase(
                it,
                newNamaBarang,
                newTipeTenda,
                newUkuranBarang,
                newFrameTenda,
                newPasakTenda,
                newWarnaBarang,
                newStockBarang,
                newHargaBarang,
                newCaraPemasangan,
                newKegunaanBarang,
                it.gambar,
//                context
            )
        }

    }

    private fun uploadToDatabase(
        it: Barang,
        newNamaBarang: String,
        newTipeTenda: String,
        newUkuranBarang: String,
        newFrameTenda: String,
        newPasakTenda: String,
        newWarnaBarang: String,
        newStockBarang: String,
        newHargaBarang: String,
        newCaraPemasangan: String,
        newKegunaanBarang: String,
        image: String,
//        context: Context
    ) {
        val model =
            Barang(
                id = it.id,
                jenis = it.jenis,
                nama = newNamaBarang,
                bahan = it.bahan,
                tipe = newTipeTenda,
                ukuran = newUkuranBarang,
                frame = newFrameTenda,
                pasak = newPasakTenda,
                warna = newWarnaBarang,
                stock = newStockBarang.toInt(),
                harga = newHargaBarang.toInt(),
                caraPemasangan = newCaraPemasangan,
                kegunaanBarang = newKegunaanBarang,
                gambar = image
            )
        databaseRef.child(it.id).get().addOnSuccessListener { _ ->
            databaseRef.child(it.id).setValue(model)
//            Toast.makeText(context, "Sukses mengupdate", Toast.LENGTH_SHORT)
//                .show()
        }.addOnFailureListener {
//            Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
//                .show()
        }
    }
}