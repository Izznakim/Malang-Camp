package com.firmansyah.malangcamp.pelanggan

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.ActivityPelangganAkunBinding
import com.firmansyah.malangcamp.databinding.ActivityPelangganHomeBinding
import com.firmansyah.malangcamp.model.Pembayaran
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

//  Halaman akun profile pelanggan
class PelangganAkunActivity : AppCompatActivity() {

    private lateinit var database:FirebaseDatabase
    private lateinit var userRef:DatabaseReference
    private lateinit var storage:FirebaseStorage
    private lateinit var profilRef:StorageReference
    private lateinit var akunId:String

    companion object{
        const val EXTRA_UID="extra_uid"
    }

    private lateinit var binding: ActivityPelangganAkunBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title="Akun"

        binding = ActivityPelangganAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        akunId= intent.getStringExtra(EXTRA_UID).toString()

        database= Firebase.database
        userRef=database.getReference("users")

        storage= FirebaseStorage.getInstance()
        profilRef=storage.getReference("profil/")

        userRef.child(akunId).get().addOnSuccessListener {
            val imgProfil=it.child("fotoProfil").value
            val username=it.child("username").value
            val namaDepan=it.child("namaDepan").value
            val namaBelakang=it.child("namaBelakang").value
            val email=it.child("email").value
            val noTelp=it.child("noTelp").value

            with(binding){
                if (imgProfil!=null) {
                    Glide.with(this@PelangganAkunActivity)
                        .load(imgProfil)
                        .apply(RequestOptions())
                        .into(imgProfile)
                }
                tvUsername.text= "$username"
                tvNama.text = "$namaDepan $namaBelakang"
                tvEmail.text = "$email"
                tvNoTelp.text = "$noTelp"
                imgProfile.setOnClickListener {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT

                    startActivityForResult(intent, 1)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data

            binding.imgProfile.setImageURI(imageUri)
            if (imageUri != null) {
                profilRef.child("$akunId.jpg").putFile(imageUri).addOnSuccessListener {
                    if (it.metadata != null && it.metadata?.reference != null) {
                        val result = it.storage.downloadUrl
                        result.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            userRef.child(akunId).child("fotoProfil").setValue(imageUrl)
                            Toast.makeText(this, "Berhasil mengganti foto profil", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}