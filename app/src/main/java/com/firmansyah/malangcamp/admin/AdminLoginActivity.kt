package com.firmansyah.malangcamp.admin

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.firmansyah.malangcamp.databinding.ActivityAdminLoginBinding
import com.firmansyah.malangcamp.pelanggan.PelangganHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAdminLoginBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database= Firebase.database
        ref=database.getReference("ADMIN")

        auth= FirebaseAuth.getInstance()

        with(binding){
            btnMasuk.setOnClickListener {
                val username=etUsername.text.toString().trim()
                val password=etPassword.text.toString().trim()

                if (username.isEmpty()){
                    etUsername.error="Username harus diisi"
                    etUsername.requestFocus()
                    return@setOnClickListener
                }

                if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    etUsername.error = "Username tidak valid"
                    etUsername.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty() || password.length<6){
                    etPassword.error="Password harus lebih dari 6 karakter"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                
                loginAdmin(username,password)
            }
        }
    }

    private fun loginAdmin(username: String, password: String) {
        ref.child(username).get().addOnSuccessListener{
            if (it.child("username").value == username && it.child("password").value.toString() == password){
                Intent(this, AdminHomeActivity::class.java).also { intent ->
                    intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this,"Maaf, Anda belum terdaftar sebagai Admin",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Gagal untuk memuat data",Toast.LENGTH_SHORT).show()
        }
    }
}