package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.SingleLiveEvent
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.DatabaseReference

class BarangSewaViewModel : ViewModel() {
    private val _listBarang = MutableLiveData<List<Barang>>()
    val listBarang: LiveData<List<Barang>> = _listBarang

    internal val toast = SingleLiveEvent<String>()

    fun getListBarang(databaseRef: DatabaseReference) {
        databaseRef.get().addOnSuccessListener { snapshot ->
            val list: ArrayList<Barang> = arrayListOf()
            snapshot.children.forEach {
                val barang = it.getValue(Barang::class.java)
                if (barang != null) {
                    list.add(barang)
                }
            }
            _listBarang.value = list
        }.addOnFailureListener {
            setToast(toast, it.message)
        }
    }

    private fun setToast(toast: SingleLiveEvent<String>, e: String?) {
        toast.value = e
    }
}