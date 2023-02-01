package com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.IMAGES_LOCATION
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ListBarangViewModel : ViewModel() {
    private val storageRef = Firebase.storage.getReference(IMAGES_LOCATION)

    private val _listBarang = mutableStateOf<List<Barang>>(emptyList())
    val listBarang: State<List<Barang>> = _listBarang

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private var _errorMsg = mutableStateOf("")
    val errorMsg: State<String> = _errorMsg

    var msg by mutableStateOf("")
        private set

    fun getMsg(message: String) {
        msg = message
    }

    fun getListBarang(databaseRef: DatabaseReference) {
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

    fun deleteBarang(
        databaseRef: DatabaseReference,
        barangId: String,
        pathString: String
    ) {
        databaseRef.child(barangId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue()
                storageRef.child(pathString).delete()
                msg
            }

            override fun onCancelled(error: DatabaseError) {
                msg = error.message
            }
        })
    }
}