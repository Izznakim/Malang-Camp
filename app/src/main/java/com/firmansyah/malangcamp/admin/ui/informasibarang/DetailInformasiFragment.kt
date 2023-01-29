package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.GetImageFromGallery
import com.firmansyah.malangcamp.component.editTextDeskBarang
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.theme.MalangCampTheme

//  Halaman detail informasi barang
class DetailInformasiFragment : DialogFragment() {

    private val viewModel by viewModels<DetailInformasiViewModel>()

    private var barang: Barang? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        return ComposeView(requireContext()).apply {
            setContent {
                MalangCampTheme {
                    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
                    val bitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }
                    val launcher =
                        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                            imageUri = uri
                        }
                    val context = LocalContext.current
                    var namaBarang = ""
                    var ukuranBarang = ""
                    var tipeTenda = ""
                    var frameTenda = ""
                    var pasakTenda = ""
                    var warnaBarang = ""
                    var stockBarang = ""
                    var hargaBarang = ""
                    var caraPemasangan = ""
                    var kegunaanBarang = ""
                    var bahanBarang = ""

                    barang?.let {
                        namaBarang = it.nama
                        ukuranBarang = it.ukuran
                        tipeTenda = it.tipe
                        frameTenda = it.frame
                        pasakTenda = it.pasak
                        warnaBarang = it.warna
                        stockBarang = it.stock.toString()
                        hargaBarang = it.harga.toString()
                        caraPemasangan = it.caraPemasangan
                        kegunaanBarang = it.kegunaanBarang
                        bahanBarang = it.bahan
                    }

                    var newNamaBarang by rememberSaveable { mutableStateOf(namaBarang) }
                    var newUkuranBarang by rememberSaveable { mutableStateOf(ukuranBarang) }
                    var newTipeTenda by rememberSaveable { mutableStateOf(tipeTenda) }
                    var newFrameTenda by rememberSaveable { mutableStateOf(frameTenda) }
                    var newPasakTenda by rememberSaveable { mutableStateOf(pasakTenda) }
                    var newWarnaBarang by rememberSaveable { mutableStateOf(warnaBarang) }
                    var newStockBarang by rememberSaveable { mutableStateOf(stockBarang) }
                    var newHargaBarang by rememberSaveable { mutableStateOf(hargaBarang) }
                    var newCaraPemasangan by rememberSaveable { mutableStateOf(caraPemasangan) }
                    var newKegunaanBarang by rememberSaveable { mutableStateOf(kegunaanBarang) }

                    val basicDeskBarangEmpty =
                        newNamaBarang.isEmpty() || newStockBarang.isEmpty() || newHargaBarang.isEmpty()
                    val warnaAndBahanEmpty = newWarnaBarang.isEmpty() && bahanBarang.isEmpty()
                    val whichHasUkuranEmpty = newUkuranBarang.isEmpty() ||
                            warnaAndBahanEmpty && newTipeTenda.isEmpty() ||
                            warnaAndBahanEmpty && newFrameTenda.isEmpty() ||
                            warnaAndBahanEmpty && newPasakTenda.isEmpty() ||
                            warnaAndBahanEmpty && newCaraPemasangan.isEmpty()
                    val isError =
                        basicDeskBarangEmpty || whichHasUkuranEmpty && newKegunaanBarang.isEmpty()

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
                            url = barang?.gambar,
                            contentDescription = getString(R.string.gambar_barang)
                        )
                        newNamaBarang = editTextDeskBarang(
                            deskBarang = newNamaBarang,
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
                            barang?.let {
                                Text(
                                    text = it.jenis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            when (barang?.jenis) {
                                SEPATU, JAKET, TAS, SLEEPING_BAG, TENDA -> {
                                    newUkuranBarang = editTextDeskBarang(
                                        newUkuranBarang,
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
                        when (barang?.jenis) {
                            SLEEPING_BAG -> {
                                barang?.bahan?.let {
                                    Text(
                                        text = "Berbahan $it",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        fontSize = 18.sp, textAlign = TextAlign.Center
                                    )
                                }
                            }
                            TENDA -> {
                                newTipeTenda = editTextDeskBarang(
                                    deskBarang = newTipeTenda,
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
                                    newFrameTenda = editTextDeskBarang(
                                        deskBarang = newFrameTenda,
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
                                    newPasakTenda = editTextDeskBarang(
                                        deskBarang = newPasakTenda,
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
                                newWarnaBarang = editTextDeskBarang(
                                    deskBarang = newWarnaBarang,
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
                            newStockBarang = editTextDeskBarang(
                                deskBarang = newStockBarang,
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
                            newHargaBarang = editTextDeskBarang(
                                deskBarang = newHargaBarang,
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
                        when (barang?.jenis) {
                            TENDA -> {
                                newCaraPemasangan = editTextDeskBarang(
                                    deskBarang = newCaraPemasangan,
                                    trim = false,
                                    placeholderText = stringResource(id = R.string.cara_pemasangan),
                                    titleDesk = "Cara pemasangan",
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
                                    singleLine = false,
                                    minLines = 6, maxLines = 10
                                )
                            }
                            BARANG_LAINNYA -> {
                                newKegunaanBarang = editTextDeskBarang(
                                    deskBarang = newKegunaanBarang,
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
                                onClick = { dialog?.cancel() },
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
                                    barang?.let {
                                        viewModel.updateBarang(
//                                            context,
                                            imageUri,
                                            it,
                                            newNamaBarang,
                                            newUkuranBarang,
                                            newTipeTenda,
                                            newFrameTenda,
                                            newPasakTenda,
                                            newWarnaBarang,
                                            newStockBarang,
                                            newHargaBarang,
                                            newCaraPemasangan,
                                            newKegunaanBarang
                                        )
                                    }
                                    dialog?.dismiss()
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
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            barang = if (SDK_INT >= 33) {
                arguments?.getParcelable(EXTRA_BARANG, Barang::class.java)
            } else {
                arguments?.getParcelable(EXTRA_BARANG)
            }

        }
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    companion object {
        const val EXTRA_BARANG = "extra_barang"
        const val SEPATU = "Sepatu"
        const val JAKET = "Jaket"
        const val TAS = "Tas"
        const val SLEEPING_BAG = "Sleeping Bag"
        const val TENDA = "Tenda"
        const val BARANG_LAINNYA = "Barang Lainnya"
    }

}
