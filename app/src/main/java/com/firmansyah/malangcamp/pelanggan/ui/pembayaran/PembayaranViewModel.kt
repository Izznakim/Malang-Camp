package com.firmansyah.malangcamp.pelanggan.ui.pembayaran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.SingleLiveEvent
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pelanggan
import com.google.firebase.database.DatabaseReference

class PembayaranViewModel : ViewModel() {
    private val _listBarang = MutableLiveData<List<Pelanggan.Keranjang>>()
    val listBarang: LiveData<List<Pelanggan.Keranjang>> = _listBarang

    internal val toast = SingleLiveEvent<String>()

    fun getListBarang(databaseRef: DatabaseReference) {
        databaseRef.get().addOnSuccessListener { snapshot ->
            val list: ArrayList<Pelanggan.Keranjang> = arrayListOf()
            snapshot.children.forEach {
                val barang = it.getValue(Pelanggan.Keranjang::class.java)
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