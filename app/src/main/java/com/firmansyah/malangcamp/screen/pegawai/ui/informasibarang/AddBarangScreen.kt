package com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang

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
import androidx.compose.material.ScaffoldState
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
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG_LAINNYA
import com.firmansyah.malangcamp.other.ConstVariable.Companion.JAKET
import com.firmansyah.malangcamp.other.ConstVariable.Companion.SEPATU
import com.firmansyah.malangcamp.other.ConstVariable.Companion.SLEEPING_BAG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.TAS
import com.firmansyah.malangcamp.other.ConstVariable.Companion.TENDA
import com.firmansyah.malangcamp.theme.MalangCampTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun AddBarangScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
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
                    titleDesk = stringResource(id = R.string.nama_barang),
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    )
                )
                stockBarang = editTextDeskBarang(
                    stockBarang,
                    true,
                    stringResource(id = R.string.stock),
                    titleDesk = stringResource(id = R.string.stock_barang),
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                hargaBarang = editTextDeskBarang(
                    hargaBarang,
                    true,
                    stringResource(id = R.string.rp_harga_per_hari),
                    titleDesk = stringResource(id = R.string.harga_barang),
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            } else {
                ErrorText(text = stringResource(R.string.anda_belum_memilih_gambarnya))
            }

            when (jenisBarang) {
                SEPATU, JAKET, TAS -> {
                    ukuranBarang = editTextDeskBarang(
                        ukuranBarang,
                        false,
                        stringResource(id = R.string.ukuran),
                        stringResource(id = R.string.ukuran_barang),
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Next
                        )
                    )
                    warnaBarang = editTextDeskBarang(
                        warnaBarang,
                        false,
                        stringResource(id = R.string.warna),
                        titleDesk = stringResource(id = R.string.warna_barang),
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
                SLEEPING_BAG -> {
                    ukuranBarang = editTextDeskBarang(
                        ukuranBarang,
                        true,
                        stringResource(id = R.string.ukuran),
                        stringResource(id = R.string.ukuran_barang),
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
                TENDA -> {
                    ukuranBarang = editTextDeskBarang(
                        ukuranBarang,
                        false,
                        stringResource(id = R.string.ukuran),
                        titleDesk = stringResource(R.string.ukuran_tenda),
                        KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    tipeBarang = editTextDeskBarang(
                        tipeBarang,
                        false,
                        stringResource(id = R.string.tipe),
                        titleDesk = stringResource(R.string.tipe_tenda),
                        KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    frameBarang = editTextDeskBarang(
                        frameBarang,
                        true,
                        stringResource(id = R.string.frame),
                        titleDesk = stringResource(R.string.frame_tenda),
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    pasakBarang = editTextDeskBarang(
                        pasakBarang,
                        true,
                        stringResource(id = R.string.pasak),
                        titleDesk = stringResource(R.string.pasak_tenda),
                        KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    caraPemasangan = editTextDeskBarang(
                        caraPemasangan,
                        false,
                        stringResource(id = R.string.cara_pemasangan),
                        titleDesk = stringResource(R.string.cara_untuk_memasang_tenda),
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
                BARANG_LAINNYA -> {
                    kegunaanBarang = editTextDeskBarang(
                        kegunaanBarang,
                        false,
                        stringResource(id = R.string.kegunaan_barang),
                        stringResource(R.string.kegunaan_pada_barang),
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
                addBarangViewModel,
                context,
                scaffoldState,
                coroutineScope
            )
        }
    }
}