package com.firmansyah.malangcamp.pelanggan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.admin.AdminHomeActivity
import com.firmansyah.malangcamp.databinding.ActivityPelangganLoginBinding
import com.google.firebase.auth.FirebaseAuth

class PelangganLoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPelangganLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPelangganLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                Intent(this@PelangganLoginActivity,PelangganRegisterActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    private fun loginPelanggan(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Intent(this, PelangganHomeActivity::class.java).also { intent ->
                        intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(this,it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}