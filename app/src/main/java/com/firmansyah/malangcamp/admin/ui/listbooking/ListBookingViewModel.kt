package com.firmansyah.malangcamp.admin.ui.listbooking

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.SingleLiveEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ListBookingViewModel : ViewModel() {
    private val _listBooking = mutableStateOf<List<Pembayaran>>(emptyList())
    val listBooking: State<List<Pembayaran>> = _listBooking

    internal val toast = SingleLiveEvent<String>()

    fun getListBooking(databaseRef: DatabaseReference) {
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<Pembayaran> = arrayListOf()
                snapshot.children.forEach {
                    if (it.child("status").value == "netral") {
                        val pembayaran = it.getValue(Pembayaran::class.java)
                        if (pembayaran != null) {
                            list.add(pembayaran)
                        }
                    }
                }
                _listBooking.value = list
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