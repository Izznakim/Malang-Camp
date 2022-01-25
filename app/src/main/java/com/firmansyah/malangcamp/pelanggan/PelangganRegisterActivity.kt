package com.firmansyah.malangcamp.pelanggan

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firmansyah.malangcamp.databinding.ActivityPelangganRegisterBinding
import com.firmansyah.malangcamp.model.Pelanggan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PelangganRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPelangganRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPelangganRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        ref = database.getReference("users")

        auth = FirebaseAuth.getInstance()

        bnd()
    }

    private fun bnd() {
        with(binding) {
            btnDaftar.setOnClickListener {
                val username = etUsername.text.toString().trim()
                val namaDepan = etNamaDepan.text.toString().trim()
                val namaBelakang = etNamaBelakang.text.toString().trim()
                val noTelp = etNoTelp.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val ulangiPassword = etUlangiPassword.text.toString().trim()

//                Username
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

//                Nama Depan
                if (namaDepan.isEmpty()) {
                    etNamaDepan.error = "Nama Depan harus diisi"
                    etNamaDepan.requestFocus()
                    return@setOnClickListener
                }

                if (Patterns.EMAIL_ADDRESS.matcher(namaDepan).matches()) {
                    etNamaDepan.error = "Nama Depan tidak valid"
                    etNamaDepan.requestFocus()
                    return@setOnClickListener
                }

//                Nama Belakang
                if (Patterns.EMAIL_ADDRESS.matcher(namaBelakang).matches()) {
                    etNamaBelakang.error = "Nama Belakang tidak valid"
                    etNamaBelakang.requestFocus()
                    return@setOnClickListener
                }

//                Nomor Telepon
                if (noTelp.isEmpty()) {
                    etNoTelp.error = "Nomor telepon harus diisi"
                    etNoTelp.requestFocus()
                    return@setOnClickListener
                }

                if (!Patterns.PHONE.matcher(noTelp).matches()) {
                    etNoTelp.error = "Nomor telepon tidak valid"
                    etNoTelp.requestFocus()
                    return@setOnClickListener
                }
//                E-mail
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

//                Password
                if (password.isEmpty() || password.length < 6) {
                    etPassword.error = "Password harus lebih dari 6 karakter"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }

                if (ulangiPassword != password) {
                    etUlangiPassword.error = "Password yang anda masukkan tidak sama"
                    etUlangiPassword.requestFocus()
                    return@setOnClickListener
                }

                registerPelanggan(username, email, namaDepan, namaBelakang, noTelp, password)
            }

            tvLogin.setOnClickListener {
                Intent(this@PelangganRegisterActivity, PelangganLoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun registerPelanggan(
        username: String,
        email: String,
        namaDepan: String,
        namaBelakang: String,
        noTelp: String,
        password: String
    ) {
        binding.progressBar.visibility= View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val id = auth.currentUser?.uid
                    if (id != null) {
                        val model =
                            Pelanggan(id, username, email, namaDepan, namaBelakang, noTelp)
                        ref.child(id).get().addOnSuccessListener {
                            Toast.makeText(this, "Pembuatan akun sukses", Toast.LENGTH_SHORT)
                                .show()
                            ref.child(id).setValue(model)
                            Intent(this, PelangganHomeActivity::class.java).also { intent ->
                                    binding.progressBar.visibility= View.GONE
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        }.addOnFailureListener { e->
                                binding.progressBar.visibility= View.GONE
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                            binding.progressBar.visibility= View.GONE
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}