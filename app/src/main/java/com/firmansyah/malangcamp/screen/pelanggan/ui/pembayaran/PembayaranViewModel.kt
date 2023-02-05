package com.firmansyah.malangcamp.screen.pelanggan.ui.pembayaran

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BUKTI_LOCATION
import com.firmansyah.malangcamp.other.ConstVariable.Companion.KERANJANG_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PEMBAYARAN
import com.firmansyah.malangcamp.other.ConstVariable.Companion.USERS_PATH
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PembayaranViewModel : ViewModel() {

    private val auth = Firebase.auth

    private val keranjangRef =
        Firebase.database.getReference("$USERS_PATH/${auth.currentUser?.uid}/$KERANJANG_PATH")
    private val pembayaranRef = Firebase.database.getReference(PEMBAYARAN)
    private val barangRef = Firebase.database.getReference(BARANG)

    private val storageRef = Firebase.storage.getReference(BUKTI_LOCATION)

    private val _listBarang = mutableStateOf<List<Keranjang>>(emptyList())
    val listBarang: State<List<Keranjang>> = _listBarang

    private val _toastMessage = mutableStateOf("")
    var toastMessage: State<String> = _toastMessage

    private val _showToast = mutableStateOf(false)
    var showToast: State<Boolean> = _showToast

    fun getListBarang() {
        keranjangRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<Keranjang> = arrayListOf()
                snapshot.children.forEach {
                    val barang = it.getValue(Keranjang::class.java)
                    if (barang != null) {
                        list.add(barang)
                    }
                }
                _listBarang.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                _showToast.value = true
                _toastMessage.value = error.message
            }

        })
    }

    fun sewa(
        imageUri: Uri?,
        tanggalPengambilan: String,
        tanggalPengembalian: String,
        jamPengambilan: String,
        selisihHari: Int,
        namaPenyewa: String,
        nomorTelepon: String,
        total: Int,
        listSewa: java.util.ArrayList<Keranjang>,
        listStock: java.util.ArrayList<Int>,
        textMsg: String
    ) {
        val idAkun = auth.currentUser?.uid
        val idPembayar = pembayaranRef.push().key
        val buktiRef = storageRef.child("${idPembayar}.jpg")

        if (idAkun != null && idPembayar != null) {
            imageUri?.let {
                buktiRef.putFile(it).addOnSuccessListener { task ->
                    if (task.metadata != null && task.metadata?.reference != null) {
                        val result = task.storage.downloadUrl
                        result.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val model = Pembayaran(
                                idAkun,
                                idPembayar,
                                tanggalPengambilan,
                                tanggalPengembalian,
                                jamPengambilan,
                                selisihHari,
                                namaPenyewa,
                                nomorTelepon,
                                imageUrl,
                                total * selisihHari,
                                ConstVariable.NETRAL,
                                listSewa
                            )
                            pembayaranRef.child(idPembayar)
                                .setValue(model)
                            _showToast.value = true
                            _toastMessage.value = textMsg
                            keranjangRef.removeValue()
                        }
                    }
                }.addOnFailureListener { e ->
                    _showToast.value = true
                    _toastMessage.value = e.message.toString()
                }
            }

            for (i in listSewa.indices) {
                barangRef.child(listSewa[i].idBarang).child(
                    ConstVariable.STOCK_PATH
                )
                    .setValue(listStock[i] - listSewa[i].jumlah)
            }
        }
    }
}