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

// Halaman home
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}