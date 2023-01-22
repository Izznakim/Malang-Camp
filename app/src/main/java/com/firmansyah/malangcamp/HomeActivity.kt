package com.firmansyah.malangcamp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.firmansyah.malangcamp.component.ButtonAdmin
import com.firmansyah.malangcamp.component.ButtonPelanggan
import com.firmansyah.malangcamp.component.LogoMalangCamp
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.firmansyah.malangcamp.theme.darkGreen

// Halaman home
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MalangCampTheme {
                Home()
            }
        }
    }
}

@Composable
fun Home() {
    val context = LocalContext.current
    Surface(
        color = darkGreen,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoMalangCamp()
            ButtonAdmin(context)
            ButtonPelanggan(context)
        }
    }
}