package com.firmansyah.malangcamp.component

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import coil.compose.AsyncImage
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ZoomImageActivity

@Composable
fun LogoMalangCamp() {
    Image(
        painter = painterResource(id = R.drawable.logo_mc),
        contentDescription = stringResource(id = R.string.logo_malang_camp),
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(226.dp)
    )
}

@Composable
fun LogoLoginAdmin() {
    Image(
        painter = painterResource(id = R.drawable.mountains),
        contentDescription = stringResource(id = R.string.login_admin),
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(146.dp)
    )
}

@Composable
fun BuktiPembayaranImage(pembayaran: Pembayaran?, activity: FragmentActivity?) {
    Text(
        text = stringResource(id = R.string.text_bukti_pembayaran),
        modifier = Modifier.padding(top = 16.dp)
    )
    AsyncImage(
        model = pembayaran?.buktiPembayaran,
        contentDescription = stringResource(id = R.string.foto_bukti_pembayaran),
        modifier = Modifier
            .padding(
                top = 8.dp
            )
            .height(200.dp)
            .fillMaxWidth()
            .clickable {
                Intent(
                    activity,
                    ZoomImageActivity::class.java
                ).also {
                    it.putExtra(
                        ZoomImageActivity.EXTRA_IMAGE,
                        pembayaran?.buktiPembayaran
                    )
                    activity?.startActivity(it)
                }
            }
            .border(
                width = 1.dp,
                color = Color.Black
            ),
        contentScale = ContentScale.Inside,
        placeholder = painterResource(
            id = R.drawable.ic_photo
        )
    )
}
