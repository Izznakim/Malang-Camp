package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.JUMLAH_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.KERANJANG_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.USERS_PATH
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DetailBarangSewaViewModel : ViewModel() {
    private val auth = Firebase.auth

    private val keranjangRef =
        Firebase.database.getReference("$USERS_PATH/${auth.currentUser?.uid}/$KERANJANG_PATH")

    var barang by mutableStateOf(Barang())
        private set

    private val _inBasket = mutableStateOf(false)
    var inBasket: State<Boolean> = _inBasket

    private val _jumlah = mutableStateOf("")
    var jumlah: State<String> = _jumlah

    private val _toastMessage = mutableStateOf("")
    var toastMessage: State<String> = _toastMessage

    private val _showToast = mutableStateOf(false)
    var showToast: State<Boolean> = _showToast

    init {
        loadJumlahOnStart()
    }

    fun getBarang(mBarang: Barang?) {
        if (mBarang != null) {
            barang = mBarang
        }
    }

    private fun loadJumlahOnStart() {
        keranjangRef.get().addOnSuccessListener {
            if (it.exists()) {
                _inBasket.value = true
                _jumlah.value = it.child(barang.id).child(JUMLAH_PATH).value.toString()
            }
        }.addOnFailureListener {
            _toastMessage.value = it.message.toString()
            _showToast.value = true
        }
    }
}