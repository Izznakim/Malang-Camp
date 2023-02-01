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

class AddBarangViewModel : ViewModel() {
    private val storageRef = Firebase.storage.getReference(IMAGES_LOCATION)

    private val databaseRef = Firebase.database.getReference(BARANG)

    var msg by mutableStateOf("")
        private set

    fun getMsg(message: String) {
        msg = message
    }

    fun uploadToFirebase(
        uri: Uri?, jenisBarang: String,
        namaBarang: String,
        bahanBarang: String,
        tipeBarang: String,
        ukuranBarang: String,
        frameBarang: String,
        pasakBarang: String,
        warnaBarang: String,
        stockBarang: String,
        hargaBarang: String,
        caraPemasangan: String,
        kegunaanBarang: String
    ) {
        val id = databaseRef.push().key
        val fileRef =
            storageRef.child("${id}.jpg")
        if (uri != null && id != null) {
            fileRef.putFile(uri).addOnSuccessListener {
                if (it.metadata != null && it.metadata?.reference != null) {
                    val result = it.storage.downloadUrl
                    result.addOnSuccessListener { uri ->
                        val model = Barang(
                            id = id,
                            jenis = jenisBarang,
                            nama = namaBarang,
                            bahan = bahanBarang,
                            tipe = tipeBarang,
                            ukuran = ukuranBarang,
                            frame = frameBarang,
                            pasak = pasakBarang,
                            warna = warnaBarang,
                            stock = stockBarang.toInt(),
                            harga = hargaBarang.toInt(),
                            caraPemasangan = caraPemasangan,
                            kegunaanBarang = kegunaanBarang,
                            gambar = uri.toString()
                        )
                        databaseRef.child(id).setValue(model)
                        msg
                    }
                }
            }.addOnFailureListener {
                msg = it.message.toString()
            }
        }
    }
}