package com.firmansyah.malangcamp.pelanggan.ui.pembayaran

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.other.CustomTimePickerDialog
import com.firmansyah.malangcamp.adapter.KeranjangAdapter
import com.firmansyah.malangcamp.databinding.FragmentPembayaranBinding
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class PembayaranFragment : Fragment() {

    private lateinit var pembayaranViewModel: PembayaranViewModel
    private var _binding: FragmentPembayaranBinding? = null
    private lateinit var adapter: KeranjangAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var keranjangRef: DatabaseReference
    private lateinit var pembayaranRef: DatabaseReference
    private lateinit var barangRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var listSewa: ArrayList<Keranjang>
    private lateinit var listReady: ArrayList<Boolean>
    private lateinit var listStock: ArrayList<Int>

    private var tanggalPengambilan: String = ""
    private var jamPengambilan:String=""
    private var namaPenyewa: String = ""
    private var noTelp: String = ""
    private var total: Int = 0
    private var totalH: Int = 0
    private var imageUri: Uri? = null
    private var ready: Boolean? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pembayaranViewModel =
            ViewModelProvider(this)[PembayaranViewModel::class.java]

        _binding = FragmentPembayaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        database = Firebase.database
        keranjangRef = database.getReference("users/${auth.currentUser?.uid}/keranjang")
        pembayaranRef = database.getReference("pembayaran")
        barangRef = database.getReference("barang")

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("bukti/")

        listSewa = arrayListOf()
        listReady = arrayListOf()
        listStock = arrayListOf()

        initAdapter()
        viewModel()
        buttonTglJam()
        buttonGaleri()
        buttonSewa()
    }

    @SuppressLint("SetTextI18n")
    private fun buttonTglJam() {
        val kalender = Calendar.getInstance()
        val dateFormat = "dd/MM/yyyy"
        val timeFormat = "HH:mm"
        val date = SimpleDateFormat(dateFormat, Locale.getDefault())
        val time = SimpleDateFormat(timeFormat, Locale.getDefault())

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, tahun, bulan, tanggal ->
                kalender.set(Calendar.YEAR, tahun)
                kalender.set(Calendar.MONTH, bulan)
                kalender.set(Calendar.DAY_OF_MONTH, tanggal)
            }
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, jam, menit ->
                kalender.set(Calendar.HOUR_OF_DAY, jam)
                kalender.set(Calendar.MINUTE, menit)

                if (jam in 7..19){
                    binding.btnTgl.text =
                        "Diambil pada tanggal: ${date.format(kalender.time)}\nPada jam: ${
                            time.format(
                                kalender.time
                            )
                        }"
                    tanggalPengambilan = date.format(kalender.time)
                    jamPengambilan = time.format(kalender.time)
                }else {
                    binding.btnTgl.text =
                        "HARUS DISAAT JAM KERJA\n(07:00 - 20:00)"
                    return@OnTimeSetListener
                }
            }

        binding.btnTgl.setOnClickListener {
            CustomTimePickerDialog(
                requireContext(), timeSetListener,
                kalender.get(Calendar.HOUR_OF_DAY),
                kalender.get(Calendar.MINUTE),
                true
            ).show()
            DatePickerDialog(
                requireContext(), dateSetListener,
                kalender.get(Calendar.YEAR),
                kalender.get(Calendar.MONTH),
                kalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun buttonSewa() {
        binding.btnSewa.setOnClickListener {
            for (i in listReady.indices) {
                if (!listReady[i]) {
                    ready = false
                    break
                } else {
                    ready = true
                }
            }

            if (ready == true) {
                with(binding) {
                    namaPenyewa = etNamaPenyewa.text.toString()
                    noTelp = etNoTelp.text.toString()

                    when {
                        tanggalPengambilan.isEmpty() && jamPengambilan.isEmpty() -> Toast.makeText(
                            activity,
                            "Tanggal pengambilan dan jamnya harus diisi",
                            Toast.LENGTH_LONG
                        ).show()
                        etHari.text.isEmpty() -> etHari.error = "Lama penyewaan harus diisi"
                        namaPenyewa.isEmpty() -> textInputLayout2.error = "Nama Penyewa harus diisi"
                        noTelp.isEmpty() -> textInputLayout3.error = "Nomor Telepon harus diisi"
                        imageUri != null -> uploadToFirebase(imageUri)
                        imageUri == null -> Toast.makeText(
                            activity,
                            "Anda belum memilih gambarnya",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    if (namaPenyewa.isNotEmpty()) {
                        textInputLayout2.isErrorEnabled = false
                    }
                    if (noTelp.isNotEmpty()) {
                        textInputLayout3.isErrorEnabled = false
                    }
                }
            } else {
                Toast.makeText(activity, "Maaf, ada stok yang belum siap", Toast.LENGTH_LONG)
                    .show()
                listReady.clear()
                pengecekanStok()
            }
        }
    }

    private fun uploadToFirebase(imageUri: Uri?) {
        val idAkun = auth.currentUser?.uid
        val idPembayar = pembayaranRef.push().key
        val buktiRef = storageRef.child("${idPembayar}.jpg")

        if (idAkun != null && idPembayar != null) {
            when {
                imageUri != null -> withImageUri(idAkun, idPembayar, buktiRef, imageUri)
            }
            for (i in listSewa.indices) {
                barangRef.child(listSewa[i].idBarang).child("stock")
                    .setValue(listStock[i] - listSewa[i].jumlah)
            }
        }

    }

    private fun withImageUri(
        idAkun: String,
        idPembayar: String,
        buktiRef: StorageReference,
        imageUri: Uri
    ) {
        buktiRef.putFile(imageUri).addOnSuccessListener {
            if (it.metadata != null && it.metadata?.reference != null) {
                val result = it.storage.downloadUrl
                result.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    val model = Pembayaran(
                        idAkun,
                        idPembayar,
                        tanggalPengambilan,
                        jamPengambilan,
                        binding.etHari.text.toString().toInt(),
                        namaPenyewa,
                        noTelp,
                        imageUrl,
                        totalH,
                        "netral",
                        listSewa
                    )
                    pembayaranRef.child(idPembayar).setValue(model)
                    Toast.makeText(activity, "Anda telah menyewa", Toast.LENGTH_LONG)
                        .show()
                    clear()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun clear() {
        listReady.clear()
        activity?.supportFragmentManager?.popBackStack()
        keranjangRef.removeValue()
    }

    private fun buttonGaleri() {
        binding.imgBukti.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

            binding.imgBukti.setImageURI(imageUri)
        }
    }

    private fun initAdapter() {
        adapter = KeranjangAdapter(arrayListOf(),keranjangRef,false)
        binding.rvListBarang.layoutManager = LinearLayoutManager(activity)
        binding.rvListBarang.adapter = adapter
    }

    private fun viewModel() {

        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 0
        currencyFormat.currency = Currency.getInstance("IDR")

        with(pembayaranViewModel) {
            getListBarang(keranjangRef)
            listBarang.observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter.setData(it)
                }

                for (i in it.indices) {
                    total += it[i].subtotal
                    val keranjang = Keranjang()
                    keranjang.idBarang = it[i].idBarang
                    keranjang.namaBarang = it[i].namaBarang
                    keranjang.hargaBarang = it[i].hargaBarang
                    keranjang.jumlah = it[i].jumlah
                    keranjang.subtotal = it[i].subtotal
                    listSewa.add(keranjang)
                }

                pengecekanStok()

                binding.etHari.doOnTextChanged { text, _, _, _ ->
                    totalH = 0
                    when {
                        text.isNullOrEmpty() || text.toString().toInt() <= 0 -> totalH = total * 0
                        text.toString().toInt() > 0 -> totalH = total * text.toString().toInt()
                    }

                    binding.tvTotal.text = currencyFormat.format(totalH)
                }
            })
            toast.observe(viewLifecycleOwner, {
                if (it != null) {
                    val toast = it.format(this)
                    Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun pengecekanStok() {
        for (i in listSewa.indices) {
            barangRef.child(listSewa[i].idBarang).child("stock").get()
                .addOnSuccessListener { snapshot ->
                    val isReady: Boolean
                    val value = snapshot.getValue<Int>()

                    isReady = value != null && listSewa[i].jumlah <= value

                    listReady.add(isReady)
                    listStock.add(value.toString().toInt())
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}