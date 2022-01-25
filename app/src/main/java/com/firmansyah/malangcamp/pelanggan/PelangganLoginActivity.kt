package com.firmansyah.malangcamp.pelanggan

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firmansyah.malangcamp.admin.AdminHomeActivity
import com.firmansyah.malangcamp.databinding.ActivityPelangganLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PelangganLoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPelangganLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPelangganLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database= FirebaseDatabase.getInstance()
        ref=database.getReference("users")

        auth= FirebaseAuth.getInstance()

        with(binding){
            btnMasuk.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

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

                loginPelanggan(email,password)
            }

            tvRegister.setOnClickListener {
                Intent(this@PelangganLoginActivity, PelangganRegisterActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun loginPelanggan(email: String, password: String) {
        binding.progressBar.visibility= View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val id = auth.currentUser?.uid
                    if (id != null) {
                        ref.child(id).get().addOnSuccessListener { snapshot ->
                                    binding.progressBar.visibility= View.GONE
                            if (!snapshot.child("isAdmin").exists()) {
                                Intent(
                                    this@PelangganLoginActivity,
                                    PelangganHomeActivity::class.java
                                ).also { intent ->
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }
                            } else {
                                binding.progressBar.visibility= View.GONE
                                Toast.makeText(
                                    this,
                                    "Pelanggan belum terdaftar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.addOnFailureListener{ e->
                            binding.progressBar.visibility= View.GONE
                            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    binding.progressBar.visibility= View.GONE
                    Toast.makeText(this,it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val idAuth = auth.currentUser?.uid
        if (idAuth != null) {
            binding.progressBar.visibility= View.VISIBLE
            ref.child(idAuth).get().addOnSuccessListener { snapshot ->
                        binding.progressBar.visibility= View.GONE
                if (!snapshot.child("isAdmin").exists()) {
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
                binding.progressBar.visibility= View.GONE
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}