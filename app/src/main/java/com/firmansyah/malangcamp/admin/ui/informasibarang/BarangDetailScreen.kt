package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.GetImageFromGallery
import com.firmansyah.malangcamp.component.editTextDeskBarang
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.theme.MalangCampTheme

@Composable
fun BarangDetailScreen(
    navController: NavHostController,
    barang: Barang?,
    barangDetailViewModel: BarangDetailViewModel = viewModel()
) {
    MalangCampTheme {
        barangDetailViewModel.getBarang(barang)
        val mBarang = barangDetailViewModel.barang
        var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
        val bitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                imageUri = uri
            }
        val context = LocalContext.current

        var namaBarang = mBarang.nama
        var ukuranBarang = mBarang.ukuran
        var tipeTenda = mBarang.tipe
        var frameTenda = mBarang.frame
        var pasakTenda = mBarang.pasak
        var warnaBarang = mBarang.warna
        var stockBarang = mBarang.stock.toString()
        var hargaBarang = mBarang.harga.toString()
        var caraPemasangan = mBarang.caraPemasangan
        var kegunaanBarang = mBarang.kegunaanBarang

        val basicDeskBarangEmpty =
            namaBarang.isEmpty() || stockBarang.isEmpty() || hargaBarang.isEmpty()
        val warnaAndBahanEmpty = warnaBarang.isEmpty() && mBarang.bahan.isEmpty()
        val whichHasUkuranEmpty = ukuranBarang.isEmpty() ||
                warnaAndBahanEmpty && tipeTenda.isEmpty() ||
                warnaAndBahanEmpty && frameTenda.isEmpty() ||
                warnaAndBahanEmpty && pasakTenda.isEmpty() ||
                warnaAndBahanEmpty && caraPemasangan.isEmpty()
        val isError =
            basicDeskBarangEmpty || whichHasUkuranEmpty && kegunaanBarang.isEmpty()

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            GetImageFromGallery(
                launcher = launcher,
                imgUri = imageUri,
                bitmap = bitmap,
                context = context,
                url = mBarang.gambar,
                contentDescription = context.getString(R.string.gambar_barang)
            )
            namaBarang = editTextDeskBarang(
                deskBarang = namaBarang,
                trim = false,
                placeholderText = stringResource(id = R.string.nama),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                titleDesk = "Nama Barang",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textStyle = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mBarang.jenis,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                when (mBarang.jenis) {
                    SEPATU, JAKET, TAS, SLEEPING_BAG, TENDA -> {
                        ukuranBarang = editTextDeskBarang(
                            ukuranBarang,
                            false,
                            stringResource(id = R.string.ukuran),
                            "Ukuran barang",
                            KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
            when (mBarang.jenis) {
                SLEEPING_BAG -> {
                    Text(
                        text = "Berbahan ${mBarang.bahan}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        fontSize = 18.sp, textAlign = TextAlign.Center
                    )
                }
                TENDA -> {
                    tipeTenda = editTextDeskBarang(
                        deskBarang = tipeTenda,
                        trim = false,
                        placeholderText = stringResource(id = R.string.tipe),
                        titleDesk = "Tipe tenda", keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        frameTenda = editTextDeskBarang(
                            deskBarang = frameTenda,
                            trim = true,
                            placeholderText = stringResource(id = R.string.frame),
                            titleDesk = "Frame tenda", modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(end = 8.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                        pasakTenda = editTextDeskBarang(
                            deskBarang = pasakTenda,
                            trim = true,
                            placeholderText = stringResource(id = R.string.pasak),
                            titleDesk = "Pasak tenda", modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 8.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                }
                SEPATU, JAKET, TAS -> {
                    warnaBarang = editTextDeskBarang(
                        deskBarang = warnaBarang,
                        trim = false,
                        placeholderText = stringResource(id = R.string.warna),
                        titleDesk = "Warna barang",
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                stockBarang = editTextDeskBarang(
                    deskBarang = stockBarang,
                    trim = true,
                    placeholderText = stringResource(id = R.string.stock),
                    titleDesk = "Stock barang",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .weight(1f)
                )
                hargaBarang = editTextDeskBarang(
                    deskBarang = hargaBarang,
                    trim = true,
                    placeholderText = stringResource(id = R.string.harga),
                    titleDesk = "Harga barang",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                        .weight(1f)
                )
            }
            when (mBarang.jenis) {
                TENDA -> {
                    caraPemasangan = editTextDeskBarang(
                        deskBarang = caraPemasangan,
                        trim = false,
                        placeholderText = stringResource(id = R.string.cara_pemasangan),
                        titleDesk = "Cara pemasangan",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
                        singleLine = false,
                        minLines = 6, maxLines = 10
                    )
                }
                BARANG_LAINNYA -> {
                    kegunaanBarang = editTextDeskBarang(
                        deskBarang = kegunaanBarang,
                        trim = false,
                        placeholderText = stringResource(id = R.string.kegunaan_barang),
                        titleDesk = "Kegunaan barang",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
                        singleLine = false,
                        minLines = 6, maxLines = 10
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 8.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }

                Button(
                    onClick = {
                        mBarang.let {
                            barangDetailViewModel.updateBarang(
                                //                                            context,
                                imageUri,
                                it,
                                namaBarang,
                                ukuranBarang,
                                tipeTenda,
                                frameTenda,
                                pasakTenda,
                                warnaBarang,
                                stockBarang,
                                hargaBarang,
                                caraPemasangan,
                                kegunaanBarang
                            )
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 8.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    enabled = !isError
                ) {
                    Text(text = stringResource(id = R.string.update))
                }
            }
        }
    }
}

const val SEPATU = "Sepatu"
const val JAKET = "Jaket"
const val TAS = "Tas"
const val SLEEPING_BAG = "Sleeping Bag"
const val TENDA = "Tenda"
const val BARANG_LAINNYA = "Barang Lainnya"

