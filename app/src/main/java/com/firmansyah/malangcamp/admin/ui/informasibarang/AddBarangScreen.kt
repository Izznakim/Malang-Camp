package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.theme.MalangCampTheme

@Composable
fun AddBarangScreen(
    navController: NavHostController,
    addBarangViewModel: AddBarangViewModel = viewModel()
) {
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val bitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val context = LocalContext.current

    val drwbl = AppCompatResources.getDrawable(context, R.drawable.ic_add_photo)
    val jenisList by remember {
        mutableStateOf(
            listOf(
                context.getString(R.string.sepatu),
                context.getString(R.string.jaket),
                context.getString(R.string.tas),
                context.getString(R.string.sleeping_bag),
                context.getString(R.string.tenda),
                context.getString(R.string.barang_lainnya)
            )
        )
    }
    val bahanList by remember {
        mutableStateOf(
            listOf(
                context.getString(R.string.polar), context.getString(R.string.bulu)
            )
        )
    }

    var jenisBarang by rememberSaveable { mutableStateOf("") }
    var namaBarang by rememberSaveable { mutableStateOf("") }
    var stockBarang by rememberSaveable { mutableStateOf("") }
    var hargaBarang by rememberSaveable { mutableStateOf("") }
    var ukuranBarang by rememberSaveable { mutableStateOf("") }
    var warnaBarang by rememberSaveable { mutableStateOf("") }
    var bahanBarang by rememberSaveable { mutableStateOf("") }
    var tipeBarang by rememberSaveable { mutableStateOf("") }
    var frameBarang by rememberSaveable { mutableStateOf("") }
    var pasakBarang by rememberSaveable { mutableStateOf("") }
    var caraPemasangan by rememberSaveable { mutableStateOf("") }
    var kegunaanBarang by rememberSaveable { mutableStateOf("") }

    MalangCampTheme {
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            GetImageFromGallery(
                launcher = launcher,
                imgUri = imageUri,
                bitmap = bitmap,
                context = context,
                drwbl = drwbl,
                contentDescription = context.getString(R.string.tombol_gambar_untuk_menambahkan_gambar)
            )

            if (imageUri != null) {
                jenisBarang = radioButtonJnsBhnBarang(jenisList, jenisBarang)
                namaBarang = editTextDeskBarang(
                    namaBarang,
                    false,
                    stringResource(id = R.string.nama),
                    titleDesk = "Nama barang",
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
                stockBarang = editTextDeskBarang(
                    stockBarang,
                    true,
                    stringResource(id = R.string.stock),
                    titleDesk = "Stock barang",
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                hargaBarang = editTextDeskBarang(
                    hargaBarang,
                    true,
                    stringResource(id = R.string.rp_harga_per_hari),
                    titleDesk = "Harga barang",
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            } else {
                ErrorText(text = "Anda belum memilih gambarnya")
            }

            when (jenisBarang) {
                "Sepatu", "Jaket", "Tas" -> {
                    ukuranBarang = editTextDeskBarang(
                        ukuranBarang,
                        false,
                        stringResource(id = R.string.ukuran),
                        "Ukuran barang",
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Next
                        )
                    )
                    warnaBarang = editTextDeskBarang(
                        warnaBarang,
                        false,
                        stringResource(id = R.string.warna),
                        titleDesk = "Warna barang",
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        )
                    )
                    bahanBarang = ""
                    tipeBarang = ""
                    frameBarang = ""
                    pasakBarang = ""
                    caraPemasangan = ""
                    kegunaanBarang = ""
                }
                "Sleeping Bag" -> {
                    ukuranBarang = editTextDeskBarang(
                        ukuranBarang,
                        true,
                        stringResource(id = R.string.ukuran),
                        "Ukuran barang",
                        KeyboardOptions(
                            imeAction = ImeAction.Done,
                            capitalization =
                            KeyboardCapitalization.Characters
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.bahan),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    bahanBarang = radioButtonJnsBhnBarang(
                        bahanList, bahanBarang, Modifier.fillMaxWidth()
                    )
                    warnaBarang = ""
                    tipeBarang = ""
                    frameBarang = ""
                    pasakBarang = ""
                    caraPemasangan = ""
                    kegunaanBarang = ""
                }
                "Tenda" -> {
                    ukuranBarang = editTextDeskBarang(
                        ukuranBarang,
                        false,
                        stringResource(id = R.string.ukuran),
                        titleDesk = "Ukuran tenda",
                        KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    tipeBarang = editTextDeskBarang(
                        tipeBarang,
                        false,
                        stringResource(id = R.string.tipe),
                        titleDesk = "Tipe tenda",
                        KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    frameBarang = editTextDeskBarang(
                        frameBarang,
                        true,
                        stringResource(id = R.string.frame),
                        titleDesk = "Frame tenda",
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    pasakBarang = editTextDeskBarang(
                        pasakBarang,
                        true,
                        stringResource(id = R.string.pasak),
                        titleDesk = "Pasak tenda",
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    caraPemasangan = editTextDeskBarang(
                        caraPemasangan,
                        false,
                        stringResource(id = R.string.cara_pemasangan),
                        titleDesk = "Cara untuk memasang tenda",
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.None
                        ),
                        singleLine = false,
                        maxLines = 10,
                        minLines = 6
                    )
                    bahanBarang = ""
                    warnaBarang = ""
                    kegunaanBarang = ""
                }
                "Barang Lainnya" -> {
                    kegunaanBarang = editTextDeskBarang(
                        kegunaanBarang,
                        false,
                        stringResource(id = R.string.kegunaan_barang),
                        "Kegunaan pada barang",
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.None
                        ),
                        singleLine = false,
                        maxLines = 10,
                        minLines = 6
                    )
                    ukuranBarang = ""
                    bahanBarang = ""
                    warnaBarang = ""
                    tipeBarang = ""
                    frameBarang = ""
                    pasakBarang = ""
                    caraPemasangan = ""
                }
            }

            val basicDeskBarangEmpty =
                jenisBarang.isEmpty() || namaBarang.isEmpty() || stockBarang.isEmpty() || hargaBarang.isEmpty()
            val warnaAndBahanEmpty = warnaBarang.isEmpty() && bahanBarang.isEmpty()
            val whichHasUkuranEmpty = ukuranBarang.isEmpty() ||
                    warnaAndBahanEmpty && tipeBarang.isEmpty() ||
                    warnaAndBahanEmpty && frameBarang.isEmpty() ||
                    warnaAndBahanEmpty && pasakBarang.isEmpty() ||
                    warnaAndBahanEmpty && caraPemasangan.isEmpty()
            val isError =
                basicDeskBarangEmpty || whichHasUkuranEmpty && kegunaanBarang.isEmpty()

            SubmitBarangButton(
                imageUri,
                jenisBarang,
                namaBarang,
                bahanBarang,
                tipeBarang,
                ukuranBarang,
                frameBarang,
                pasakBarang,
                warnaBarang,
                stockBarang,
                hargaBarang,
                caraPemasangan,
                kegunaanBarang,
                isError,
                navController,
                addBarangViewModel
            )
        }
    }
}