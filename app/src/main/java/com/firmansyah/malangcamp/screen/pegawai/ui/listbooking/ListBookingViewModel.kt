package com.firmansyah.malangcamp.screen.pegawai.ui.listbooking

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable.Companion.NETRAL
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PEMBAYARAN
import com.firmansyah.malangcamp.other.ConstVariable.Companion.STATUS_PATH
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListBookingViewModel : ViewModel() {
    private val ref = Firebase.database.getReference(PEMBAYARAN)

    private var _listBooking = mutableStateOf<List<Pembayaran>>(emptyList())
    val listBooking: State<List<Pembayaran>> = _listBooking

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private var _errorMsg = mutableStateOf("")
    val errorMsg: State<String> = _errorMsg

    fun getListBooking() {
        _isLoading.value = true
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: ArrayList<Pembayaran> = arrayListOf()
                snapshot.children.forEach {
                    if (it.child(STATUS_PATH).value == NETRAL) {
                        val pembayaran = it.getValue(Pembayaran::class.java)
                        if (pembayaran != null) {
                            list.add(pembayaran)
                        }
                    }
                }
                _listBooking.value = list
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
                _isError.value = true
                _errorMsg.value = error.message
            }

        })
    }
}