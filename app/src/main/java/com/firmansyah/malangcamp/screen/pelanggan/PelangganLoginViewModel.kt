package com.firmansyah.malangcamp.screen.pelanggan

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.other.ConstVariable.Companion.IS_PEGAWAI_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.USERS_PATH
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PelangganLoginViewModel : ViewModel() {
    private val database = Firebase.database.getReference(USERS_PATH)

    private val auth = Firebase.auth

    private var _errorText = mutableStateOf("")
    val errorText: State<String> = _errorText

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _isIntent = mutableStateOf(false)
    val isIntent: State<Boolean> = _isIntent

    init {
        getPelangganOnStart()
    }

    private fun getPelangganOnStart() {
        val idAuth = auth.currentUser?.uid
        if (idAuth != null) {
            _isLoading.value = true
            database.child(idAuth).get().addOnSuccessListener { snapshot ->
                if (!snapshot.child(IS_PEGAWAI_PATH).exists()) {
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
        } else {
            _isLoading.value = false
        }
    }

    fun getPelanggan(email: String, password: String, blmDftrText: String) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val id = auth.currentUser?.uid
                    if (id != null) {
                        database.child(id).get().addOnSuccessListener { snapshot ->
                            if (!snapshot.child(IS_PEGAWAI_PATH).exists()) {
                                _isLoading.value = false
                                _isIntent.value = true
                                _errorText.value = ""
                            } else {
                                _isLoading.value = false
                                _errorText.value = blmDftrText
                            }
                        }.addOnFailureListener { e ->
                            _isLoading.value = false
                            _errorText.value = e.message.toString()
                        }
                    }
                } else {
                    _isLoading.value = false
                    _errorText.value = "${it.exception?.message}"
                }
            }
    }
}