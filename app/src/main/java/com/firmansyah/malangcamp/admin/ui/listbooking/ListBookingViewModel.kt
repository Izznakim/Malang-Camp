package com.firmansyah.malangcamp.admin.ui.listbooking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.SingleLiveEvent
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pelanggan
import com.google.firebase.database.DatabaseReference

class ListBookingViewModel : ViewModel() {
    private val _listPelanggan = MutableLiveData<List<Pelanggan>>()
    val listPelanggan: LiveData<List<Pelanggan>> = _listPelanggan

    internal val toast = SingleLiveEvent<String>()

    fun getListPelanggan(databaseRef: DatabaseReference) {
        databaseRef.get().addOnSuccessListener { snapshot ->
            val list: ArrayList<Pelanggan> = arrayListOf()
            snapshot.children.forEach {
                if (!it.child("isAdmin").exists()) {
                    val pelanggan = it.getValue(Pelanggan::class.java)
                    if (pelanggan != null) {
                        list.add(pelanggan)
                    }
                }
            }
            _listPelanggan.value = list
        }.addOnFailureListener {
            setToast(toast, it.message)
        }
    }

    private fun setToast(toast: SingleLiveEvent<String>, e: String?) {
        toast.value = e
    }
}