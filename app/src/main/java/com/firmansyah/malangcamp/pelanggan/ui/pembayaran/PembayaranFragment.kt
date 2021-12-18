package com.firmansyah.malangcamp.pelanggan.ui.pembayaran

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.KeranjangAdapter
import com.firmansyah.malangcamp.databinding.FragmentPembayaranBinding
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pembayaran
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
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
    private lateinit var listSewa: ArrayList<Pembayaran.BarangSewa>

    private var namaPenyewa: String = ""
    private var noTelp: Int = 0
    private var total: Int = 0
    private var imageUri: Uri? = null
    private var imageBitmap: Bitmap? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pembayaranViewModel =
            ViewModelProvider(this).get(PembayaranViewModel::class.java)

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

        initAdapter()
        viewModel()
        buttonCam()
        btnSewa()
    }

    private fun btnSewa() {
        binding.btnSewa.setOnClickListener {
            with(binding) {
                namaPenyewa = etNamaPenyewa.text.toString()
                noTelp = etNoTelp.text.toString().trim().toInt()

                if (namaPenyewa.isEmpty()) {
                    Toast.makeText(activity, "Nama Penyewa harus diisi", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if (noTelp.toString().isEmpty()) {
                    Toast.makeText(activity, "Nomor Telepon harus diisi", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            for (i in listSewa.indices) {
                barangRef.child(listSewa[i].idBarang).child("stock").get().addOnSuccessListener {
                    val value = it.value
                    if (value != null) {
                        val mStock = value.toString().toInt()
                        barangRef.child(listSewa[i].idBarang).child("stock")
                            .setValue(mStock - listSewa[i].jumlah)
                    }
                }
            }

            val baos = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            when {
                imageUri != null -> uploadToFirebase(imageUri, null)
                imageBitmap != null -> uploadToFirebase(null, data)
                else -> Toast.makeText(activity, "Anda belum memilih gambarnya", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun uploadToFirebase(imageUri: Uri?, imageBitmap: ByteArray?) {
        val idAkun = auth.currentUser?.uid
        val idPembayar = pembayaranRef.push().key
        val buktiRef = storageRef.child("${idPembayar}.jpg")
        if (idAkun != null && idPembayar != null) {
            when {
                imageUri != null -> withImageUri(idAkun, idPembayar, buktiRef, imageUri)
                imageBitmap != null -> withImageBitmap(idAkun, idPembayar, buktiRef, imageBitmap)
            }
        }
    }

    private fun withImageBitmap(
        idAkun: String,
        idPembayar: String,
        buktiRef: StorageReference,
        imageBitmap: ByteArray
    ) {
        buktiRef.putBytes(imageBitmap).addOnSuccessListener {
            if (it.metadata != null && it.metadata?.reference != null) {
                val result = it.storage.downloadUrl
                result.addOnSuccessListener { bitmap ->
                    val imageUrl = bitmap.toString()
                    val model = Pembayaran(
                        idAkun,
                        idPembayar,
                        namaPenyewa,
                        noTelp,
                        imageUrl,
                        total,
                        listSewa
                    )
                    pembayaranRef.child(idPembayar).setValue(model)
                    Toast.makeText(activity, "Anda telah menyewa", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG)
                .show()
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
                        namaPenyewa,
                        noTelp,
                        imageUrl,
                        total,
                        listSewa
                    )
                    pembayaranRef.child(idPembayar).setValue(model)
                    Toast.makeText(activity, "Anda telah menyewa", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun buttonCam() {
        binding.btnGaleri.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(intent, 1)
        }
        binding.btnCamera.setOnClickListener {
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE

            startActivityForResult(intent, 2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

            binding.imgBukti.setImageURI(imageUri)
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap

            binding.imgBukti.setImageBitmap(imageBitmap)
        }
    }

    private fun initAdapter() {
        adapter = KeranjangAdapter(arrayListOf())
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
                    val sewa = Pembayaran.BarangSewa()
                    sewa.idBarang = it[i].idBarang
                    sewa.namaBarang = it[i].namaBarang
                    sewa.hargaBarang = it[i].hargaBarang
                    sewa.jumlah = it[i].jumlah
                    sewa.hari = it[i].hari
                    sewa.subtotal = it[i].subtotal
                    listSewa.add(sewa)
                }

                binding.tvTotal.text = currencyFormat.format(total)
            })
            toast.observe(viewLifecycleOwner, {
                if (it != null) {
                    val toast = it.format(this)
                    Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}