package com.firmansyah.malangcamp.pelanggan.ui.menu

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.theme.MalangCampTheme

//  Halaman peraturan sewa menyewa
class PeraturanSewaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MalangCampTheme {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.aturan_dan_syarat_sewa),
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.aturan1),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.aturan2),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.aturan3),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.aturan4),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.aturan5),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.pemesanan_via_aplikasi),
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.pemesanan1),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.pemesanan2),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.NmerTlpnPegawai),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .clickable {
                                val phone =
                                    this@PeraturanSewaActivity.getString(R.string.NmerTlpnPegawai)
                                val packageManager = this@PeraturanSewaActivity.packageManager
                                val intent = Intent(Intent.ACTION_VIEW)

                                if (packageManager != null) {
                                    try {
                                        val url = "https://api.whatsapp.com/send?phone=$phone"
                                        intent.setPackage("com.whatsapp")
                                        intent.data = Uri.parse(url)
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                            this@PeraturanSewaActivity.startActivity(intent)
                                        } else {
                                            this@PeraturanSewaActivity.startActivity(intent)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            },
                        textAlign = TextAlign.Center,
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}