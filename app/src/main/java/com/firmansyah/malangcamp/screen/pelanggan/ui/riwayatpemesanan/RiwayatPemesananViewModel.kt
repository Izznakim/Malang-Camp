package com.firmansyah.malangcamp.screen.pelanggan.ui.riwayatpemesanan

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PEMBAYARAN
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RiwayatPemesananViewModel : ViewModel() {

    private val auth = Firebase.auth

    private val databaseRef = Firebase.database.getReference(PEMBAYARAN)

    private val _listRiwayat = mutableStateOf<List<Pembayaran>>(emptyList())
    val listRiwayat: State<List<Pembayaran>> = _listRiwayat

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private var _errorMsg = mutableStateOf("")
    val errorMsg: State<String> = _errorMsg

    fun getListRiwayat() {
        _isLoading.value = true
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<Pembayaran> = arrayListOf()
                snapshot.children.forEach {
                    if (it.child("idAkun").value == auth.currentUser?.uid) {
                        val pembayaran = it.getValue(Pembayaran::class.java)
                        if (pembayaran != null) {
                            list.add(pembayaran)
                        }
                    }
                }
                _isLoading.value = false
                _listRiwayat.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = error.message
            }

        })
    }
}