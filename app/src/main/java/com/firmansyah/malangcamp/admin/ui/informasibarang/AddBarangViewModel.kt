package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AddBarangViewModel : ViewModel() {
    private val storageRef = Firebase.storage.getReference("images/")

    private val databaseRef = Firebase.database.getReference("barang")

//    private var _errorMsg = MutableLiveData<String>()
//    val errorMsg: LiveData<String> = _errorMsg

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
//                        _errorMsg.value = "Berhasil menambahkan barang"
                    }
                }
            }.addOnFailureListener {
//                _errorMsg.value = "${it.message}"
            }
        }
    }
}