package com.firmansyah.malangcamp.admin.ui.listbooking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.SingleLiveEvent
import com.firmansyah.malangcamp.model.Pembayaran
import com.google.firebase.database.DatabaseReference

class ListBookingViewModel : ViewModel() {
    private val _listBooking = MutableLiveData<List<Pembayaran>>()
    val listBooking: LiveData<List<Pembayaran>> = _listBooking

    internal val toast = SingleLiveEvent<String>()

    fun getListBooking(databaseRef: DatabaseReference) {
        databaseRef.get().addOnSuccessListener { snapshot ->
            val list: ArrayList<Pembayaran> = arrayListOf()
            snapshot.children.forEach {
                val pembayaran = it.getValue(Pembayaran::class.java)
                if (pembayaran != null) {
                    list.add(pembayaran)
                }
            }
            _listBooking.value = list
        }.addOnFailureListener {
            it.message?.let { e -> setToast(toast, e) }
        }
    }

    private fun setToast(toast: SingleLiveEvent<String>, e: String) {
        toast.value = e
    }
}