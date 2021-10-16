package com.firmansyah.malangcamp.pelanggan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.firmansyah.malangcamp.databinding.ActivityPelangganRegisterBinding
import com.firmansyah.malangcamp.model.Pelanggan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PelangganRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPelangganRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPelangganRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database= FirebaseDatabase.getInstance()
        ref=database.getReference("PELANGGAN")

        auth= FirebaseAuth.getInstance()

        bnd()
    }

    private fun bnd() {
        with(binding){
            btnDaftar.setOnClickListener {
                val username=etUsername.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val ulangiPassword=etUlangiPassword.text.toString().trim()

                if (username.isEmpty()) {
                    etUsername.error = "Username harus diisi"
                    etUsername.requestFocus()
                    return@setOnClickListener
                }

                if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    etUsername.error = "Username tidak valid"
                    etUsername.requestFocus()
                    return@setOnClickListener
                }

                if (username.contains(" ")) {
                    etUsername.error = "Spasi bisa diganti dengan underscore (_)"
                    etUsername.requestFocus()
                    return@setOnClickListener
                }

                if (email.isEmpty()) {
                    etEmail.error = "Email harus diisi"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.error = "Email tidak valid"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty() || password.length < 6) {
                    etPassword.error = "Password harus lebih dari 6 karakter"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }

                if (ulangiPassword!=password){
                    etUlangiPassword.error = "Password yang anda masukkan tidak sama"
                    etUlangiPassword.requestFocus()
                    return@setOnClickListener
                }

                registerPelanggan(username,email,password)
            }

            tvLogin.setOnClickListener {
                Intent(this@PelangganRegisterActivity,PelangganLoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun registerPelanggan(username:String,email: String, password: String) {
        val id=ref.push().key
        val model= Pelanggan(username, email, password, id)

        ref.child(username).get().addOnSuccessListener {
            if (it.exists()){
                Toast.makeText(this,"Maaf, Username sudah digunakan oleh pengguna lain", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Pembuatan akun sukses", Toast.LENGTH_SHORT).show()
                ref.child(username).setValue(model)
                Intent(this, PelangganHomeActivity::class.java).also { intent ->
                    intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Gagal untuk memuat data",Toast.LENGTH_SHORT).show()
        }
    }
}