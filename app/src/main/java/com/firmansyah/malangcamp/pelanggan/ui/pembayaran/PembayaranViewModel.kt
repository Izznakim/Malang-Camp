package com.firmansyah.malangcamp.pelanggan.ui.pembayaran

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.SingleLiveEvent
import com.firmansyah.malangcamp.model.Keranjang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class PembayaranViewModel : ViewModel() {
    private val _listBarang = MutableLiveData<List<Keranjang>>()
    val listBarang: LiveData<List<Keranjang>> = _listBarang

    internal val toast = SingleLiveEvent<String>()

    fun getListBarang(databaseRef: DatabaseReference) {
        databaseRef.addValueEventListener(object: ValueEventListener {

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
                setToast(toast, error.message)
            }

        })
    }

    private fun setToast(toast: SingleLiveEvent<String>, e: String) {
        toast.value = e
    }
}