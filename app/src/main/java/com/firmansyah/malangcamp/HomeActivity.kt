package com.firmansyah.malangcamp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.component.ButtonHome
import com.firmansyah.malangcamp.component.Logo
import com.firmansyah.malangcamp.pelanggan.PelangganLoginActivity
import com.firmansyah.malangcamp.screen.pegawai.PegawaiLoginActivity
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.firmansyah.malangcamp.theme.darkGreen

// Halaman home
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            MalangCampTheme {
                Home(context)
            }
        }
    }
}

@Composable
fun Home(context: Context) {
    Surface(
        color = darkGreen,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo(
                drwbl = R.drawable.logo_mc,
                contentDesc = R.string.logo_malang_camp,
                modifier = Modifier.size(226.dp)
            )
            ButtonHome(
                context,
                Intent(context, PegawaiLoginActivity::class.java),
                56.dp,
                R.string.pegawai
            )
            ButtonHome(
                context,
                Intent(context, PelangganLoginActivity::class.java),
                20.dp,
                R.string.pelanggan
            )
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    val context = LocalContext.current
    MalangCampTheme {
        Home(context)
    }
}