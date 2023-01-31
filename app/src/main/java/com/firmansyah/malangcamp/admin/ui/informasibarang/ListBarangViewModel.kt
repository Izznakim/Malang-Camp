package com.firmansyah.malangcamp.admin.ui.informasibarang

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.SingleLiveEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ListBarangViewModel : ViewModel() {
    private val storageRef = Firebase.storage.getReference("images/")

    private val _listBarang = mutableStateOf<List<Barang>>(emptyList())
    val listBarang: State<List<Barang>> = _listBarang

    internal val toast = SingleLiveEvent<String>()

    fun getListBarang(databaseRef: DatabaseReference) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<Barang> = arrayListOf()
                snapshot.children.forEach {
                    val barang = it.getValue(Barang::class.java)
                    if (barang != null) {
                        list.add(barang)
                    }
                }
                _listBarang.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(toast, error.message)
            }

        })
    }

    fun deleteBarang(databaseRef: DatabaseReference, model: Barang) {
        databaseRef.child(model.id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.ref.removeValue()
                storageRef.child("${model.id}.jpg").delete()
                setToast(toast, "${model.nama} telah dihapus")
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(toast, error.message)
            }
        })
    }

    private fun setToast(toast: SingleLiveEvent<String>, e: String) {
        toast.value = e
    }
}