package com.firmansyah.malangcamp.pelanggan.ui.pembayaran

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.databinding.FragmentPembayaranBinding
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DATE_FORMAT
import com.firmansyah.malangcamp.other.ConstVariable.Companion.KERANJANG_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.STOCK_PATH
import com.firmansyah.malangcamp.other.ConstVariable.Companion.TIME_FORMAT
import com.firmansyah.malangcamp.other.ConstVariable.Companion.USERS_PATH
import com.firmansyah.malangcamp.other.CustomTimePickerDialog
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.firmansyah.malangcamp.theme.black
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

//  Halaman pembayaran
class PembayaranFragment : Fragment() {

    private lateinit var pembayaranViewModel: PembayaranViewModel
    private var _binding: FragmentPembayaranBinding? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var keranjangRef: DatabaseReference
    private lateinit var barangRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pembayaranViewModel =
            ViewModelProvider(this)[PembayaranViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                val listSewa: ArrayList<Keranjang> = arrayListOf()
                val listReady: ArrayList<Boolean> = arrayListOf()
                val listStock: ArrayList<Int> = arrayListOf()
                var namaPenyewa by rememberSaveable { mutableStateOf("") }
                var nomorTelepon by rememberSaveable { mutableStateOf("") }
                var total = 0
                var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
                val launcher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                        imageUri = uri
                    }
                val drwbl = AppCompatResources.getDrawable(context, R.drawable.ic_add_photo)
                var ready by rememberSaveable { mutableStateOf(false) }
                var isAmbil by rememberSaveable { mutableStateOf(false) }
                var selisihSkrgAmbil by rememberSaveable { mutableStateOf(0) }
                var kalenderAmbil by rememberSaveable { mutableStateOf(0.toLong()) }
                var tanggalPengambilan by rememberSaveable { mutableStateOf("") }
                var selisihHari by rememberSaveable { mutableStateOf(0) }
                var tanggalPengembalian by rememberSaveable { mutableStateOf("") }
                var jamPengambilan by rememberSaveable { mutableStateOf("") }
                var totalH: Int

                total = getListSewaAndTotal(total, listSewa)

                pengecekanStok(listSewa, listReady, listStock)

                var namaFail by rememberSaveable { mutableStateOf(false) }
                var nomorTeleponFail by rememberSaveable { mutableStateOf(false) }

                val kalender = Calendar.getInstance()
                val dateFormat = DATE_FORMAT
                val timeFormat = TIME_FORMAT
                val date = SimpleDateFormat(dateFormat, Locale.getDefault())
                val time = SimpleDateFormat(timeFormat, Locale.getDefault())
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { _, tahun, bulan, tanggal ->
                        kalender.set(Calendar.YEAR, tahun)
                        kalender.set(Calendar.MONTH, bulan)
                        kalender.set(Calendar.DAY_OF_MONTH, tanggal)

                        if (isAmbil) {
                            selisihSkrgAmbil =
                                if (date.format(kalender.time) != date.format(Date().time)) {
                                    val diff = kalender.timeInMillis - Date().time
                                    (diff / 1000 / 60 / 60 / 24 + 1).toInt()
                                } else {
                                    0
                                }
                            kalenderAmbil = kalender.timeInMillis
                            tanggalPengambilan = date.format(kalender.time)
                        } else {
                            val diff = kalender.timeInMillis - kalenderAmbil
                            selisihHari = (diff / 1000 / 60 / 60 / 24).toInt()
                            tanggalPengembalian = date.format(kalender.time)
                        }
                    }
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { _, jam, menit ->
                        kalender.set(Calendar.HOUR_OF_DAY, jam)
                        kalender.set(Calendar.MINUTE, menit)

                        if (jam in 7..19) {
                            jamPengambilan = time.format(kalender.time)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                requireContext().getString(R.string.tombol_hapus),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@OnTimeSetListener
                        }
                    }


                MalangCampTheme {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                            .fillMaxSize()
                    ) {
                        item { ListBarangTitle() }
                        item {
                            Divider(
                                thickness = 1.dp,
                                color = black,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        listKeranjang(listSewa, true, keranjangRef, requireContext())
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(.8f)) {
                                    val columnWeight1 = .65f
                                    val columnWeight2 = .45f
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(R.string.diambil_pada_tanggal_),
                                            modifier = Modifier.weight(columnWeight1)
                                        )
                                        Text(
                                            text = if (tanggalPengambilan.isNotEmpty()) date.format(
                                                kalender.time
                                            ) else "__/__/____",
                                            modifier = Modifier
                                                .clickable {
                                                    PickTheDate(dateSetListener, kalender)

                                                    isAmbil = true
                                                }
                                                .weight(columnWeight2),
                                            textDecoration = TextDecoration.Underline,
                                            color = Color.Blue,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(R.string._dikembalikan_pada_tanggal_),
                                            modifier = Modifier.weight(columnWeight1)
                                        )
                                        Text(
                                            text = if (tanggalPengembalian.isNotEmpty()) date.format(
                                                kalender.time
                                            ) else "__/__/____",
                                            modifier = Modifier
                                                .clickable(enabled = tanggalPengambilan.isNotEmpty()) {
                                                    ReturnTheDate(
                                                        dateSetListener,
                                                        kalender,
                                                        selisihSkrgAmbil
                                                    )

                                                    isAmbil = false
                                                }
                                                .weight(columnWeight2),
                                            textDecoration = TextDecoration.Underline,
                                            color = Color.Blue,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = stringResource(R.string._pada_jam__),
                                            modifier = Modifier.weight(columnWeight1)
                                        )
                                        Text(
                                            text = if (jamPengambilan.isNotEmpty()) time.format(
                                                kalender.time
                                            ) else "__:__",
                                            modifier = Modifier
                                                .clickable {
                                                    TimePick(timeSetListener, kalender)
                                                }
                                                .weight(columnWeight2),
                                            textDecoration = TextDecoration.Underline,
                                            color = Color.Blue,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                Text(
                                    text = requireContext().getString(
                                        R.string.___Hari,
                                        selisihHari.toString()
                                    ),
                                    modifier = Modifier
                                        .weight(.2f)
                                        .fillMaxHeight(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        item {
                            totalH = total * selisihHari
                            Total(currencyIdrFormat().format(totalH))
                        }
                        item {
                            Text(
                                text = stringResource(id = R.string.cara_pembayaran),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            )
                        }
                        item {
                            namaFail = registerInput(
                                profile = namaPenyewa,
                                onProfileValueChange = { newValue -> namaPenyewa = newValue },
                                trim = false,
                                contentDescription = stringResource(R.string.nama_penyewa_error),
                                placeHolder = stringResource(id = R.string.text_nama_penyewa),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    capitalization = KeyboardCapitalization.Words
                                ),
                                paddingTop = 16.dp,
                                isError = namaPenyewa.isEmpty(),
                                errorText = stringResource(R.string.nama_penyewa_harus_diisi)
                            )
                        }
                        item {
                            nomorTeleponFail = nomorTeleponInput(
                                nomorTelepon = nomorTelepon,
                                onNoTelpValueChange = { newValue -> nomorTelepon = newValue })
                        }
                        item {
                            Text(
                                text = stringResource(id = R.string.text_bukti_pembayaran),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                        item {
                            GetImageFromGallery(
                                launcher = launcher,
                                imgUri = imageUri,
                                bitmap = null,
                                context = context,
                                drwbl = drwbl,
                                contentDescription = context.getString(R.string.tombol_gambar_untuk_menambahkan_gambar)
                            )
                        }
                        item {
                            Text(
                                text = stringResource(id = R.string.harap_foto_bukti_pembayaran_harus_jelas_jika_tidak_kemungkinan_pesanan_anda_di_tolak),
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        item {
                            Button(
                                onClick = {
                                    for (i in listReady.indices) {
                                        if (!listReady[i]) {
                                            ready = false
                                            break
                                        } else {
                                            ready = true
                                        }
                                    }

                                    if (ready) {
                                        pembayaranViewModel.sewa(
                                            imageUri,
                                            tanggalPengambilan,
                                            tanggalPengembalian,
                                            jamPengambilan,
                                            selisihHari,
                                            namaPenyewa,
                                            nomorTelepon,
                                            total,
                                            listSewa,
                                            listStock,
                                            context.getString(R.string.anda_telah_menyewa)
                                        )
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            context.getString(R.string.maaf_ada_stok_yang_belum_siap),
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                        pengecekanStok(listSewa, listReady, listStock)
                                    }
                                    tanggalPengambilan = ""
                                    tanggalPengembalian = ""
                                    jamPengambilan = ""
                                    selisihHari = 0
                                    namaPenyewa = ""
                                    nomorTelepon = ""
                                    imageUri = null
                                    listReady.clear()
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth()
                                    .wrapContentWidth(align = Alignment.CenterHorizontally),
                                enabled = !(tanggalPengambilan.isEmpty() || jamPengambilan.isEmpty() || namaFail || nomorTeleponFail || imageUri == null)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.sewa),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    if (pembayaranViewModel.showToast.value) {
                        Toast.makeText(
                            requireContext(),
                            pembayaranViewModel.toastMessage.value,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }

    private fun ReturnTheDate(
        dateSetListener: DatePickerDialog.OnDateSetListener,
        kalender: Calendar,
        selisihSkrgAmbil: Int
    ) {
        val datePickerDialog = DatePickerDialog(
            requireContext(), dateSetListener,
            kalender.get(Calendar.YEAR),
            kalender.get(Calendar.MONTH),
            kalender.get(Calendar.DAY_OF_MONTH)
        )
        val datePicker = datePickerDialog.datePicker

        datePicker.minDate =
            System.currentTimeMillis() + 1000 * 60 * 60 * 24 * (selisihSkrgAmbil + 1)
        datePickerDialog.show()
    }

    private fun PickTheDate(
        dateSetListener: DatePickerDialog.OnDateSetListener,
        kalender: Calendar
    ) {
        val datePickerDialog = DatePickerDialog(
            requireContext(), dateSetListener,
            kalender.get(Calendar.YEAR),
            kalender.get(Calendar.MONTH),
            kalender.get(Calendar.DAY_OF_MONTH)
        )
        val datePicker = datePickerDialog.datePicker

        datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun TimePick(
        timeSetListener: TimePickerDialog.OnTimeSetListener,
        kalender: Calendar
    ) {
        CustomTimePickerDialog(
            requireContext(), timeSetListener,
            kalender.get(Calendar.HOUR_OF_DAY),
            kalender.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun pengecekanStok(
        listSewa: ArrayList<Keranjang>,
        listReady: ArrayList<Boolean>,
        listStock: ArrayList<Int>
    ) {
        for (i in listSewa.indices) {
            barangRef.child(listSewa[i].idBarang).child(STOCK_PATH).get()
                .addOnSuccessListener { snapshot ->
                    val isReady: Boolean
                    val value = snapshot.getValue<Int>()

                    isReady = value != null && listSewa[i].jumlah <= value

                    listReady.add(isReady)
                    listStock.add(value.toString().toInt())
                }
        }
    }

    @Composable
    private fun getListSewaAndTotal(
        total: Int,
        listSewa: ArrayList<Keranjang>
    ): Int {
        var total1 = total
        pembayaranViewModel.getListBarang()
        pembayaranViewModel.listBarang.value.also {
            for (i in it.indices) {
                total1 += it[i].subtotal
                val keranjang = Keranjang()
                keranjang.idBarang = it[i].idBarang
                keranjang.namaBarang = it[i].namaBarang
                keranjang.hargaBarang = it[i].hargaBarang
                keranjang.jumlah = it[i].jumlah
                keranjang.subtotal = it[i].subtotal
                listSewa.add(keranjang)
            }
        }
        return total1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        database = Firebase.database
        keranjangRef = database.getReference("$USERS_PATH/${auth.currentUser?.uid}/$KERANJANG_PATH")
        barangRef = database.getReference(BARANG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}