package com.firmansyah.malangcamp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firmansyah.malangcamp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnAdmin.setOnClickListener {
                Intent(this@HomeActivity,AdminLoginActivity::class.java).also {
                    startActivity(it)
                }
            }

//            btnPelanggan masuk ke activity login pelanggan
        }
    }
}