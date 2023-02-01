package com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.IMAGES_LOCATION
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BarangDetailViewModel : ViewModel() {
    private val storageRef = Firebase.storage.getReference(IMAGES_LOCATION)
    private val databaseRef = Firebase.database.getReference(BARANG)

    var barang by mutableStateOf(Barang())
        private set

    var msg by mutableStateOf("")
        private set

    fun getBarang(mBarang: Barang?) {
        if (mBarang != null) {
            barang = mBarang
        }
    }

    fun getMsg(message: String) {
        msg = message
    }

    fun updateBarang(
        uri: Uri?,
        barang: Barang,
        pathString: String,
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
        val fileRef = storageRef.child(pathString)
        if (uri != null) {
            fileRef.putFile(uri).addOnSuccessListener { task ->
                if (task.metadata != null && task.metadata?.reference != null) {
                    val result = task.storage.downloadUrl
                    result.addOnSuccessListener { mUri ->
                        imageUri = mUri
                        uploadToDatabase(
                            barang,
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
                            imageUri.toString()
                        )
                    }
                }
            }
        } else {
            uploadToDatabase(
                barang,
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
                barang.gambar,
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
            msg
        }.addOnFailureListener {
            msg = it.message.toString()
        }
    }
}