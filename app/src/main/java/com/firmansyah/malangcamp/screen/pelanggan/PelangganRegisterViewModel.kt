package com.firmansyah.malangcamp.screen.pelanggan

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.model.Pelanggan
import com.firmansyah.malangcamp.other.ConstVariable.Companion.USERS_PATH
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PelangganRegisterViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val database = Firebase.database.getReference(USERS_PATH)

    private var _msgText = mutableStateOf("")
    val msgText: State<String> = _msgText

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _isIntent = mutableStateOf(false)
    val isIntent: State<Boolean> = _isIntent

    fun registerPelanggan(
        username: String,
        email: String,
        namaDepan: String,
        namaBelakang: String,
        noTelp: String,
        password: String,
        successText: String
    ) {
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val id = auth.currentUser?.uid
                    if (id != null) {
                        val model =
                            Pelanggan(id, username, email, namaDepan, namaBelakang, noTelp)
                        database.child(id).get().addOnSuccessListener {
                            _msgText.value = successText
                            database.child(id).setValue(model)
                            _isLoading.value = false
                            _isIntent.value = true
                        }.addOnFailureListener { e ->
                            _isLoading.value = false
                            _msgText.value = e.message.toString()
                        }
                    }
                } else {
                    _isLoading.value = false
                    _msgText.value = it.exception?.message.toString()
                }
            }
    }
}