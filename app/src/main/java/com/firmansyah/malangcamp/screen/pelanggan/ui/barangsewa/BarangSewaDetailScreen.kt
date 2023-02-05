package com.firmansyah.malangcamp.screen.pelanggan.ui.barangsewa

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.ConstVariable
import com.firmansyah.malangcamp.other.ZoomImageActivity
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.other.rating
import com.firmansyah.malangcamp.theme.MalangCampTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BarangSewaDetailScreen(
    navController: NavHostController,
    barang: Barang?,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    viewModel: BarangSewaDetailViewModel = viewModel()
) {
    viewModel.getBarang(barang)
    val context = LocalContext.current
    val mBarang = viewModel.barang
    var jumlah by rememberSaveable { mutableStateOf(0) }
    if (viewModel.inBasket.value) {
        jumlah = viewModel.jumlah.value.toInt()
    }

    MalangCampTheme {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = mBarang.gambar,
                contentDescription = stringResource(
                    id = R.string.gambar_barang
                ),
                modifier = Modifier
                    .size(184.dp)
                    .padding(top = 8.dp)
                    .clickable {
                        Intent(context, ZoomImageActivity::class.java).also {
                            it.putExtra(ConstVariable.EXTRA_IMAGE, mBarang.gambar)
                            context.startActivity(it)
                        }
                    }
            )
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = stringResource(R.string.rating_barang),
                tint = Color.Yellow,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(R.string.rate_5, rating(mBarang)),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = mBarang.nama,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 16.dp)
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = mBarang.jenis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                when (mBarang.jenis) {
                    ConstVariable.SEPATU, ConstVariable.JAKET, ConstVariable.TAS, ConstVariable.SLEEPING_BAG -> {
                        Text(
                            text = stringResource(R.string.ukuran___, mBarang.ukuran),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 4.dp, end = 8.dp)
                        )
                    }
                }
            }
            when (mBarang.jenis) {
                ConstVariable.SLEEPING_BAG -> {
                    TextDetailBarangSewa(
                        stringResource(
                            R.string.bahan___,
                            mBarang.bahan
                        )
                    )
                }
                ConstVariable.TENDA -> {
                    Text(
                        text = stringResource(R.string.ukuran___, mBarang.ukuran),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 4.dp, end = 8.dp)
                    )
                    TextDetailBarangSewa(stringResource(R.string.tipe___, mBarang.tipe))
                    RowPaddingTop8dp(
                        stringResource(R.string.frame___, mBarang.frame),
                        stringResource(R.string.pasak___, mBarang.pasak)
                    )
                }
                ConstVariable.SEPATU, ConstVariable.JAKET, ConstVariable.TAS -> {
                    TextDetailBarangSewa(
                        stringResource(
                            R.string.warna___,
                            mBarang.warna
                        )
                    )
                }
            }
            RowPaddingTop8dp(
                stringResource(R.string.stock___, mBarang.stock),
                currencyIdrFormat().format(mBarang.harga)
            )
            when (mBarang.jenis) {
                ConstVariable.TENDA -> {
                    ParagraphTextDeskBarang(
                        mBarang.caraPemasangan,
                        R.string.cara_pemasangan___
                    )
                }
                ConstVariable.BARANG_LAINNYA -> {
                    ParagraphTextDeskBarang(
                        mBarang.kegunaanBarang,
                        R.string.kegunaan_barang___
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        jumlah--
                    },
                    modifier = Modifier
                        .weight(2f)
                        .wrapContentSize(),
                    enabled = jumlah > 0
                ) {
                    Text(text = stringResource(id = R.string.minus))
                }
                OutlinedTextField(
                    value = jumlah.toString().trim(),
                    onValueChange = { newValue ->
                        try {
                            jumlah = when {
                                newValue.isEmpty() -> 0
                                newValue.toInt() > mBarang.stock -> mBarang.stock
                                newValue.toInt() < 0 -> 0
                                else -> newValue
                            }.toString().trim().toInt()
                        } catch (e: NumberFormatException) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.isi_dengan_angka))
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedButton(
                    onClick = {
                        jumlah++
                    },
                    modifier = Modifier
                        .weight(2f)
                        .wrapContentSize(),
                    enabled = jumlah < mBarang.stock
                ) {
                    Text(text = stringResource(id = R.string.plus))
                }
            }
            Button(onClick = {
                if (jumlah > 0) {
                    navController.popBackStack()
                    coroutineScope.launch {
                        viewModel.putToKeranjang(
                            mBarang, jumlah, context.getString(
                                R.string.___telah_ditambahkan_ke_keranjang,
                                mBarang.nama
                            )
                        )
                        scaffoldState.snackbarHostState.showSnackbar(viewModel.msg.value)
                    }
                }
            }, modifier = Modifier.padding(top = 16.dp), enabled = jumlah != 0) {
                Text(text = stringResource(id = R.string.tambah_ke_keranjang))
            }
        }
    }
}

@Composable
private fun ParagraphTextDeskBarang(deskBarang: String, deskBarangId: Int) {
    Text(
        text = buildAnnotatedString {
            deskBarang.split("\n").forEachIndexed { index, value ->
                if (index == 0) {
                    append(
                        stringResource(
                            deskBarangId,
                            "\u2022 $value"
                        )
                    )
                } else {
                    append("\n\u2022 $value")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
}

@Composable
private fun RowPaddingTop8dp(deskBarang1: String, deskBarang2: String) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = deskBarang1,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 8.dp)
        )
        Text(
            text = deskBarang2,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 4.dp, end = 8.dp)
        )
    }
}

@Composable
private fun TextDetailBarangSewa(deskBarang: String) {
    Text(
        text = deskBarang,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
}