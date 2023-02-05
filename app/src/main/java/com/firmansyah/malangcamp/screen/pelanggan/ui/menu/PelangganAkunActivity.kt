package com.firmansyah.malangcamp.screen.pelanggan.ui.menu

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.LinearProgressBar
import com.firmansyah.malangcamp.other.ConstVariable.Companion.EXTRA_UID
import com.firmansyah.malangcamp.other.ConstVariable.Companion.GALLERY_IMAGE
import com.firmansyah.malangcamp.theme.MalangCampTheme

//  Halaman akun profile pelanggan
class PelangganAkunActivity : ComponentActivity() {
    private val viewModel by viewModels<PelangganAkunViewModel>()

    private lateinit var akunId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        akunId = intent.getStringExtra(EXTRA_UID).toString()

        setContent {
            viewModel.getUser(akunId)
            var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
            var bitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }
            val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                    imageUri = uri
                }
            if (imageUri != null) {
                viewModel.uploadToFirebase(
                    akunId,
                    imageUri!!,
                    getString(R.string.berhasil_mengganti_foto_profil)
                )
                if (viewModel.showToast.value) {
                    Toast.makeText(this, viewModel.toastMsg.value, Toast.LENGTH_SHORT).show()
                }
            }

            MalangCampTheme {
                Surface {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .verticalScroll(
                                rememberScrollState()
                            )
                    ) {
                        if (viewModel.isLoading.value) {
                            LinearProgressBar(alpha = 1f)
                        } else {
                            LinearProgressBar(alpha = 0f)
                        }
                        when {
                            imageUri != null -> {
                                imageUri?.let {
                                    bitmap = if (Build.VERSION.SDK_INT < 28) {
                                        MediaStore.Images
                                            .Media.getBitmap(
                                                this@PelangganAkunActivity.contentResolver,
                                                it
                                            )

                                    } else {
                                        val source = ImageDecoder
                                            .createSource(
                                                this@PelangganAkunActivity.contentResolver,
                                                it
                                            )
                                        ImageDecoder.decodeBitmap(source)
                                    }
                                }
                                bitmap?.asImageBitmap()?.let {
                                    Image(
                                        bitmap = it,
                                        contentDescription = stringResource(id = R.string.foto_profil),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(300.dp)
                                            .clickable {
                                                launcher.launch(GALLERY_IMAGE)
                                            }
                                    )
                                }
                            }
                            viewModel.imgUrl.isEmpty() -> {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_sharp_account_circle_24),
                                    contentDescription = stringResource(id = R.string.foto_profil),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .clickable {
                                            launcher.launch(GALLERY_IMAGE)
                                        }
                                )
                            }
                            else -> {
                                AsyncImage(
                                    model = viewModel.imgUrl,
                                    contentDescription = stringResource(id = R.string.foto_profil),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .clickable {
                                            launcher.launch(GALLERY_IMAGE)
                                        }
                                )
                            }
                        }

                        Text(
                            text = viewModel.username,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${viewModel.namaDepan} ${viewModel.namaBelakang}",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = viewModel.email,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = viewModel.noTelp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}