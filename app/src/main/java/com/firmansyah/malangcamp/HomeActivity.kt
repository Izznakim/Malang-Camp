package com.firmansyah.malangcamp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firmansyah.malangcamp.admin.AdminLoginActivity
import com.firmansyah.malangcamp.databinding.ActivityHomeBinding
import com.firmansyah.malangcamp.pelanggan.PelangganLoginActivity
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.firmansyah.malangcamp.theme.darkGreen
import com.firmansyah.malangcamp.theme.green

// Halaman home
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            composeHome.setContent {
                MaterialTheme {
                    Home()
                }
            }
        }
    }
}

@Composable
fun Home() {
    val context = LocalContext.current
    MalangCampTheme {
        Surface(color = darkGreen) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_mc),
                    contentDescription = stringResource(id = R.string.logo_malang_camp),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(226.dp)
                )
                Button(
                    onClick = {
                        Intent(context, AdminLoginActivity::class.java).also {
                            context.startActivity(it)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 56.dp)
                        .size(width = 246.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = green)
                ) {
                    Text(
                        text = "Admin",
                        style = MaterialTheme.typography.h6.copy(
                            fontSize = 18.sp
                        )
                    )
                }
                Button(
                    onClick = {
                        Intent(context, PelangganLoginActivity::class.java).also {
                            context.startActivity(it)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .size(width = 246.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = green)
                ) {
                    Text(
                        text = "Pelanggan",
                        style = MaterialTheme.typography.h6.copy(
                            fontSize = 18.sp
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    Home()
}