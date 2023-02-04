package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.ID_BARANG_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.KERANJANG_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.USERS_PATH
import com.firmansyah.malangcamp.other.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BarangSewaViewModel : ViewModel() {

    private val auth = Firebase.auth

    private val databaseRef = Firebase.database.getReference(BARANG)
    private val keranjangRef =
        Firebase.database.getReference("${USERS_PATH}/${auth.currentUser?.uid}/${KERANJANG_PATH}")

    private val _listBarang = mutableStateOf<List<Barang>>(emptyList())
    val listBarang: State<List<Barang>> = _listBarang

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private var _changedColor = mutableStateOf(false)
    val changedColor: State<Boolean> = _changedColor

    private var _errorMsg = mutableStateOf("")
    val errorMsg: State<String> = _errorMsg

    internal val toast = SingleLiveEvent<String>()

    fun getListBarang() {
        _isLoading.value = true
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<Barang> = arrayListOf()
                snapshot.children.forEach {
                    val barang = it.getValue(Barang::class.java)
                    if (barang != null) {
                        list.add(barang)
                    }
                }
                _isLoading.value = false
                _listBarang.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = error.message
            }
        })
    }

    fun changeCardColor(barang: Barang) {
        keranjangRef.child(barang.id).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val idBarang = snapshot.child(ID_BARANG_PATH).value
                    if (barang.id == idBarang) {
                        _changedColor.value = true
                    }
                } else {
                    _changedColor.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMsg.value = error.message
            }

        })
    }
}