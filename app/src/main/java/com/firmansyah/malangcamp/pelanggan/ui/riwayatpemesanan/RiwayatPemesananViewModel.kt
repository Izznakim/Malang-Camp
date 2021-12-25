package com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.SingleLiveEvent
import com.firmansyah.malangcamp.model.Pembayaran
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class RiwayatPemesananViewModel : ViewModel() {
    private val _listRiwayat = MutableLiveData<List<Pembayaran>>()
    val listRiwayat: LiveData<List<Pembayaran>> = _listRiwayat

    internal val toast = SingleLiveEvent<String>()

    fun getListRiwayat(databaseRef: DatabaseReference,auth: FirebaseAuth) {
        databaseRef.get().addOnSuccessListener { snapshot ->
            val list: ArrayList<Pembayaran> = arrayListOf()
            snapshot.children.forEach {
                if (it.child("idAkun").value == auth.currentUser?.uid){
                    val pembayaran = it.getValue(Pembayaran::class.java)
                    if (pembayaran != null) {
                        list.add(pembayaran)
                    }
                }
            }
            _listRiwayat.value = list
        }.addOnFailureListener {
            it.message?.let { e -> setToast(toast, e) }
        }
    }

    private fun setToast(toast: SingleLiveEvent<String>, e: String) {
        toast.value = e
    }
}