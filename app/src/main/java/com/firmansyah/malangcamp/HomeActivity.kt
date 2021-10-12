package com.firmansyah.malangcamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firmansyah.malangcamp.admin.AdminLoginActivity
import com.firmansyah.malangcamp.databinding.ActivityHomeBinding
import com.firmansyah.malangcamp.pelanggan.PelangganLoginActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
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