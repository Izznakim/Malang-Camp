package com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.other.SingleLiveEvent
import com.firmansyah.malangcamp.model.Pembayaran
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class RiwayatPemesananViewModel : ViewModel() {
    private val _listRiwayat = MutableLiveData<List<Pembayaran>>()
    val listRiwayat: LiveData<List<Pembayaran>> = _listRiwayat

    internal val toast = SingleLiveEvent<String>()

    fun getListRiwayat(databaseRef: DatabaseReference,auth: FirebaseAuth) {
        databaseRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
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