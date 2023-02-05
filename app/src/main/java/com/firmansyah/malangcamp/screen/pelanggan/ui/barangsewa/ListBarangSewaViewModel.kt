package com.firmansyah.malangcamp.screen.pelanggan.ui.barangsewa

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListBarangSewaViewModel : ViewModel() {

    private val databaseRef = Firebase.database.getReference(BARANG)

    private val _listBarang = mutableStateOf<List<Barang>>(emptyList())
    val listBarang: State<List<Barang>> = _listBarang

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private var _errorMsg = mutableStateOf("")
    val errorMsg: State<String> = _errorMsg

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
}