package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.SingleLiveEvent
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Keranjang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class BarangSewaViewModel : ViewModel() {
    private val _listBarang = MutableLiveData<List<Barang>>()
    val listBarang: LiveData<List<Barang>> = _listBarang

    private val _listKeranjang = MutableLiveData<List<Keranjang>>()
    val listKeranjang: LiveData<List<Keranjang>> = _listKeranjang

    internal val toast = SingleLiveEvent<String>()

    fun getListBarang(databaseRef: DatabaseReference) {
        databaseRef.addValueEventListener(object: ValueEventListener {

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

    fun getListJumlah(keranjangRef: DatabaseReference) {
        keranjangRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.value!=null){
                val list: ArrayList<Keranjang> = arrayListOf()
                snapshot.children.forEach{
                    val keranjang=it.getValue(Keranjang::class.java)
                    if (keranjang!=null){
                        list.add(keranjang)
                    }
                }
                _listKeranjang.value=list
            }
        }.addOnFailureListener {
            it.message?.let { e -> setToast(toast, e) }
        }
    }

    private fun setToast(toast: SingleLiveEvent<String>, e: String) {
        toast.value = e
    }
}