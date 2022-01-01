package com.firmansyah.malangcamp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firmansyah.malangcamp.admin.AdminHomeActivity
import com.firmansyah.malangcamp.admin.AdminLoginActivity
import com.firmansyah.malangcamp.databinding.ActivityHomeBinding
import com.firmansyah.malangcamp.pelanggan.PelangganHomeActivity
import com.firmansyah.malangcamp.pelanggan.PelangganLoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database
        ref = database.getReference("users")


        auth = Firebase.auth

        with(binding) {
            btnAdmin.setOnClickListener {
                Intent(this@HomeActivity, AdminLoginActivity::class.java).also {
                    startActivity(it)
                }
            }

            btnPelanggan.setOnClickListener {
                Intent(this@HomeActivity, PelangganLoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val idAuth = auth.currentUser?.uid
        if (idAuth != null) {
            ref.child(idAuth).get().addOnSuccessListener { snapshot ->
                if (snapshot.child("isAdmin").value == true) {
                    Intent(this, AdminHomeActivity::class.java).also { intent ->
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                } else {
                    Intent(
                        this,
                        PelangganHomeActivity::class.java
                    ).also { intent ->
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}