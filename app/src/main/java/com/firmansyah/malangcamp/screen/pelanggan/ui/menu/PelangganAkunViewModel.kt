package com.firmansyah.malangcamp.screen.pelanggan.ui.menu

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.firmansyah.malangcamp.other.ConstVariable.Companion.Email_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.FOTO_PROFIL_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.NAMA_BELAKANG_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.NAMA_DEPAN_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.NO_TELP_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PROFIL_LOCATION
import com.firmansyah.malangcamp.other.ConstVariable.Companion.USERS_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.Username_PATH
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PelangganAkunViewModel : ViewModel() {
    private val userRef = Firebase.database.getReference(USERS_PATH)
    private val profilRef = Firebase.storage.getReference(PROFIL_LOCATION)

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _showToast = mutableStateOf(false)
    val showToast: State<Boolean> = _showToast

    private var _toastMsg = mutableStateOf("")
    val toastMsg: State<String> = _toastMsg

    var imgUrl by mutableStateOf("")
        private set

    var username by mutableStateOf("")
        private set

    var namaDepan by mutableStateOf("")
        private set

    var namaBelakang by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var noTelp by mutableStateOf("")
        private set

    fun getUser(akunId: String) {
        userRef.child(akunId).get().addOnSuccessListener {
            imgUrl = it.child(FOTO_PROFIL_PATH).value.toString()
            username = it.child(Username_PATH).value.toString()
            namaDepan = it.child(NAMA_DEPAN_PATH).value.toString()
            namaBelakang = it.child(NAMA_BELAKANG_PATH).value.toString()
            email = it.child(Email_PATH).value.toString()
            noTelp = it.child(NO_TELP_PATH).value.toString()
        }.addOnFailureListener {
            _showToast.value = false
            _toastMsg.value = it.message.toString()
        }
    }

    fun uploadToFirebase(akunId: String, imageUri: Uri, successMsg: String) {
        profilRef.child("$akunId.jpg").putFile(imageUri)
            .addOnSuccessListener {
                _isLoading.value = true
                if (it.metadata != null && it.metadata?.reference != null) {
                    val result = it.storage.downloadUrl
                    result.addOnSuccessListener { uri ->
                        _isLoading.value = false
                        val imageUrl = uri.toString()
                        userRef.child(akunId).child(FOTO_PROFIL_PATH)
                            .setValue(imageUrl)
                        _showToast.value = true
                        _toastMsg.value = successMsg
                    }
                }
            }.addOnFailureListener {
                _isLoading.value = false
                _showToast.value = true
                _toastMsg.value = it.message.toString()
            }
    }
}