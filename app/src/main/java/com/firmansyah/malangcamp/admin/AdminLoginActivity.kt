package com.firmansyah.malangcamp.admin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firmansyah.malangcamp.databinding.ActivityAdminLoginBinding
import com.firmansyah.malangcamp.pelanggan.PelangganHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
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
        ref=database.getReference("users")

        auth= Firebase.auth

        with(binding){
            btnMasuk.setOnClickListener {
                val email=etEmail.text.toString().trim()
                val password=etPassword.text.toString().trim()

                if (email.isEmpty()){
                    etEmail.error="Email harus diisi"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.error = "Email tidak valid"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty() || password.length<6){
                    etPassword.error="Password harus lebih dari 6 karakter"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                
                loginAdmin(email,password)
            }
        }
    }

    private fun loginAdmin(email: String, password: String) {
        binding.progressBar.visibility= View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val id = auth.currentUser?.uid
                    if (id != null) {
                        ref.child(id).get().addOnSuccessListener{ snapshot ->
                            if (snapshot.child("isAdmin").value == true){
                                Intent(this, AdminHomeActivity::class.java).also { intent ->
                                    binding.progressBar.visibility= View.GONE
                                    intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }
                            } else {
                                binding.progressBar.visibility= View.GONE
                                Toast.makeText(this,"Maaf, Anda belum terdaftar sebagai Admin",Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener{
                            binding.progressBar.visibility= View.GONE
                            Toast.makeText(this,"Gagal untuk memuat data",Toast.LENGTH_SHORT).show()
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
                if (snapshot.child("isAdmin").exists()) {
                    Intent(this, AdminHomeActivity::class.java).also { intent ->
                        binding.progressBar.visibility= View.GONE
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