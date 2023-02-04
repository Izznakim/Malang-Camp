package com.firmansyah.malangcamp.pelanggan.ui.menu

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firmansyah.malangcamp.databinding.ActivityPeraturanSewaBinding

//  Halaman peraturan sewa menyewa
class PeraturanSewaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPeraturanSewaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeraturanSewaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etNomorWA.setOnClickListener {
            val phone=binding.etNomorWA.text
            val packageManager=this.packageManager
            val intent= Intent(Intent.ACTION_VIEW)

            if (packageManager!=null) {
                try {
                    val url = "https://api.whatsapp.com/send?phone=$phone"
                    intent.setPackage("com.whatsapp")
                    intent.data = Uri.parse(url)
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                        this.startActivity(intent)
                    }else {
                        if (intent.resolveActivity(packageManager) != null) {
                            this.startActivity(intent)
                        }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}