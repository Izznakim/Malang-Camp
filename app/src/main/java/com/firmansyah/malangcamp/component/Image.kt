package com.firmansyah.malangcamp.component

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun GetImageFromGallery(
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    imgUri: Uri?,
    bitmap: Bitmap?,
    context: Context,
    drwbl: Bitmap?
) {
    var bitmap1 = bitmap
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 16.dp),
        onClick = { launcher.launch("image/*") },
        border = BorderStroke(1.dp, Color.Black)
    ) {
        imgUri?.let {
            bitmap1 = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }

        Image(
            bitmap = (((bitmap1?.asImageBitmap() ?: drwbl?.asImageBitmap())!!)),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
    }
}
