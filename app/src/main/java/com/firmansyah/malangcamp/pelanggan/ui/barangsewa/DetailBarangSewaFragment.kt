package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG_LAINNYA
import com.firmansyah.malangcamp.other.ConstVariable.Companion.EXTRA_BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.EXTRA_IMAGE
import com.firmansyah.malangcamp.other.ConstVariable.Companion.JAKET
import com.firmansyah.malangcamp.other.ConstVariable.Companion.KERANJANG_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.SEPATU
import com.firmansyah.malangcamp.other.ConstVariable.Companion.SLEEPING_BAG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.TAS
import com.firmansyah.malangcamp.other.ConstVariable.Companion.TENDA
import com.firmansyah.malangcamp.other.ConstVariable.Companion.USERS_PATH
import com.firmansyah.malangcamp.other.ZoomImageActivity
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.other.rating
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//  Halaman detail informasi barang
class DetailBarangSewaFragment : DialogFragment() {

    private lateinit var viewModel: DetailBarangSewaViewModel

    private lateinit var database: FirebaseDatabase
    private lateinit var keranjangRef: DatabaseReference
    private lateinit var barangRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var barang: Barang? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[DetailBarangSewaViewModel::class.java]
        // Inflate the layout for this fragment
        return ComposeView(requireContext()).apply {
            setContent {
                viewModel.getBarang(barang)
                val mBarang = viewModel.barang
                var jumlah by rememberSaveable { mutableStateOf(0) }
                if (viewModel.inBasket.value) {
                    jumlah = viewModel.jumlah.value.toInt()
                }
                if (viewModel.showToast.value) {
                    Toast.makeText(
                        requireContext(),
                        viewModel.toastMessage.value,
                        Toast.LENGTH_SHORT
                    ).show()
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
                                    Intent(activity, ZoomImageActivity::class.java).also {
                                        it.putExtra(EXTRA_IMAGE, mBarang.gambar)
                                        startActivity(it)
                                    }
                                }
                        )
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(R.string.rating_barang),
                            tint = androidx.compose.ui.graphics.Color.Yellow,
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
                                SEPATU, JAKET, TAS, SLEEPING_BAG -> {
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
                            SLEEPING_BAG -> {
                                TextDetailBarangSewa(
                                    stringResource(
                                        R.string.bahan___,
                                        mBarang.bahan
                                    )
                                )
                            }
                            TENDA -> {
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
                            SEPATU, JAKET, TAS -> {
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
                            TENDA -> {
                                ParagraphTextDeskBarang(
                                    mBarang.caraPemasangan,
                                    R.string.cara_pemasangan___
                                )
                            }
                            BARANG_LAINNYA -> {
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
                                    } catch (e: java.lang.NumberFormatException) {
                                        Toast.makeText(
                                            requireContext(),
                                            context.getString(R.string.isi_dengan_angka),
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
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
                                val model = Keranjang(
                                    mBarang.id,
                                    mBarang.nama,
                                    mBarang.harga,
                                    jumlah,
                                    mBarang.harga * jumlah
                                )
                                keranjangRef.child(mBarang.id).setValue(model)
                                Toast.makeText(
                                    requireContext(),
                                    context.getString(
                                        R.string.___telah_ditambahkan_ke_keranjang,
                                        mBarang.nama
                                    ),
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog?.dismiss()
                            }
                        }, modifier = Modifier.padding(top = 16.dp), enabled = jumlah != 0) {
                            Text(text = stringResource(id = R.string.tambah_ke_keranjang))
                        }
                    }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        keranjangRef =
            database.getReference("${USERS_PATH}/${auth.currentUser?.uid}/${KERANJANG_PATH}")
        barangRef = database.getReference(BARANG)

        if (arguments != null) {
            barang = arguments?.getParcelable(EXTRA_BARANG)
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }
}