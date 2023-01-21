package com.firmansyah.malangcamp.admin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.other.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AdminLoginViewModel : ViewModel() {
    private val database = Firebase.database.getReference("users")
    private val auth = Firebase.auth

    private var _errorText = mutableStateOf("")
    val errorText: State<String> = _errorText

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _isIntent = mutableStateOf(false)
    val isIntent: State<Boolean> = _isIntent

    internal val toast = SingleLiveEvent<String>()

    init {
        getAdminStart()
    }

    private fun getAdminStart() {
        val idAuth = auth.currentUser?.uid
        if (idAuth != null) {
            _isLoading.value = true
            database.child(idAuth).get().addOnSuccessListener { snapshot ->
                if (snapshot.child("isAdmin").exists()) {
                    _isLoading.value = false
                    _isIntent.value = true
                } else {
                    _isLoading.value = false
                    _errorText.value = ""
                }
            }.addOnFailureListener { e ->
                _isLoading.value = false
                _errorText.value = "${e.message}"
            }
        }
    }

    fun getAdmin(email: String, password: String) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val id = auth.currentUser?.uid
                if (id != null) {
                    database.child(id).get().addOnSuccessListener { snapshot ->
                        if (snapshot.child("isAdmin").value == true) {
                            _isLoading.value = false
                            _isIntent.value = true
                            _errorText.value = ""
                        } else {
                            _isLoading.value = false
                            _errorText.value = "Maaf, Anda belum terdaftar sebagai Admin"
                        }
                    }.addOnFailureListener {
                        _isLoading.value = false
                        _errorText.value = "Gagal untuk memuat data"
                    }
                }
            } else {
                _isLoading.value = false
                _errorText.value = "${it.exception?.message}"
            }
        }
    }

}
