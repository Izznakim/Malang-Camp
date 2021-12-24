package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.FragmentTambahBarangBinding
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class SubmitBarangFragment : DialogFragment() {

    private var _binding: FragmentTambahBarangBinding? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference

    private var imageUri: Uri? = null
    private var jenisBarang: String = ""
    private var namaBarang: String = ""
    private var bahanBarang: String = ""
    private var tipeBarang: String = ""
    private var ukuranBarang: String = ""
    private var frameBarang: String = ""
    private var warnaBarang: String = ""
    private var pasakBarang: String = ""
    private var stockBarang:String=""
    private var hargaBarang:String=""
    private var caraPemasangan: String = ""
    private var kegunaanBarang: String = ""
    private var gambarUrl: String = ""

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentTambahBarangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable("imageUri")
            binding.imgBarang.setImageURI(imageUri)
        }

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("images/")

        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("barang")

        buttonClick()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("imageUri", imageUri)
    }

    private fun buttonClick() {
        with(binding) {

            imgBarang.setOnClickListener {
                selectImage()
            }

            rgJenisBarang.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rbSepatu, R.id.rbJaket, R.id.rbTas -> {
                        jenisBarang = when {
                            rbSepatu.isChecked -> "Sepatu"
                            rbJaket.isChecked -> "Jaket"
                            else -> "Tas"
                        }
                        visibilitySepatuJaketAtauTas()
                        allClearRgJenisBarang()
                    }
                    R.id.rbSleepingBag -> {
                        jenisBarang = "Sleeping Bag"
                        visibilitySleepingBag()
                        allClearRgJenisBarang()
                    }
                    R.id.rbTenda -> {
                        jenisBarang = "Tenda"
                        visibilityTenda()
                        allClearRgJenisBarang()
                    }
                    R.id.rbBarangLainnya -> {
                        jenisBarang = "Barang Lainnya"
                        visibilityBarangLainnya()
                        allClearRgJenisBarang()
                    }
                    else -> {
                        jenisBarang = ""
                        etNamaBarang.visibility = View.GONE
                        bahanLayout.visibility = View.GONE
                        etTipeBarang.visibility = View.GONE
                        etUkuranBarang.visibility = View.GONE
                        etFrameBarang.visibility = View.GONE
                        etWarnaBarang.visibility = View.GONE
                        etPasakBarang.visibility = View.GONE
                        etStockBarang.visibility = View.GONE
                        etHargaBarang.visibility = View.GONE
                        etCaraPemasangan.visibility = View.GONE
                        etKegunaanBarang.visibility = View.GONE
                    }
                }

                if (jenisBarang.isNotEmpty()) {
                    etNamaBarang.visibility = View.VISIBLE
                    etStockBarang.visibility = View.VISIBLE
                    etHargaBarang.visibility = View.VISIBLE
                }
            }

            rgBahanBarang.setOnCheckedChangeListener { _, checkedId ->
                bahanBarang = when (checkedId) {
                    R.id.rbPolar -> "Polar"
                    R.id.rbBulu -> "Bulu"
                    else -> ""
                }
            }

            btnCancel.setOnClickListener {
                dialog?.cancel()
            }

            btnSubmit.setOnClickListener {
                namaBarang = etNamaBarang.text.toString()
                tipeBarang = etTipeBarang.text.toString()
                ukuranBarang = etUkuranBarang.text.toString()
                frameBarang = etFrameBarang.text.toString()
                warnaBarang = etWarnaBarang.text.toString()
                pasakBarang = etPasakBarang.text.toString()
                stockBarang = etStockBarang.text.toString()
                hargaBarang = etHargaBarang.text.toString()
                caraPemasangan = etCaraPemasangan.text.toString()
                kegunaanBarang = etKegunaanBarang.text.toString()

                if (jenisBarang.isEmpty() && rgJenisBarang.isVisible) {
                    btnSubmit.error
                    btnSubmit.requestFocus()
                    Toast.makeText(
                        activity,
                        "Jenis barang harus dipilih salah satu",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                if (namaBarang.isEmpty() && etNamaBarang.isVisible) {
                    etNamaBarang.error = "Nama harus diisi"
                    etNamaBarang.requestFocus()
                    return@setOnClickListener
                }

                if (bahanBarang.isEmpty() && bahanLayout.isVisible) {
                    btnSubmit.error
                    btnSubmit.requestFocus()
                    Toast.makeText(activity, "Bahan harus diisi", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if (tipeBarang.isEmpty() && etTipeBarang.isVisible) {
                    etTipeBarang.error = "Tipe harus diisi"
                    etTipeBarang.requestFocus()
                    return@setOnClickListener
                }

                if (ukuranBarang.isEmpty() && etUkuranBarang.isVisible) {
                    etUkuranBarang.error = "Ukuran harus diisi"
                    etUkuranBarang.requestFocus()
                    return@setOnClickListener
                }

                if (frameBarang.isEmpty() && etFrameBarang.isVisible) {
                    etFrameBarang.error = "Frame harus diisi"
                    etFrameBarang.requestFocus()
                    return@setOnClickListener
                }

                if (warnaBarang.isEmpty() && etWarnaBarang.isVisible) {
                    etWarnaBarang.error = "Warna harus diisi"
                    etWarnaBarang.requestFocus()
                    return@setOnClickListener
                }

                if (pasakBarang.isEmpty() && etPasakBarang.isVisible) {
                    etPasakBarang.error = "Pasak harus diisi"
                    etPasakBarang.requestFocus()
                    return@setOnClickListener
                }

                if (caraPemasangan.isEmpty() && etCaraPemasangan.isVisible) {
                    etCaraPemasangan.error = "Cara untuk memasang tenda harus diisi"
                    etCaraPemasangan.requestFocus()
                    return@setOnClickListener
                }

                if (kegunaanBarang.isEmpty() && etKegunaanBarang.isVisible) {
                    etKegunaanBarang.error = "Kegunaan pada barang harus diisi"
                    etKegunaanBarang.requestFocus()
                    return@setOnClickListener
                }

                if (stockBarang.isEmpty() && etStockBarang.isVisible) {
                    etStockBarang.error = "Stock barang harus diisi"
                    etStockBarang.requestFocus()
                    return@setOnClickListener
                }

                if (hargaBarang.isEmpty() && etHargaBarang.isVisible) {
                    etHargaBarang.error = "Harga barang harus diisi"
                    etHargaBarang.requestFocus()
                    return@setOnClickListener
                }

                uploadData()
            }
        }
    }

    private fun visibilityBarangLainnya() {
        with(binding) {
            etUkuranBarang.visibility = View.GONE
            bahanLayout.visibility = View.GONE
            etTipeBarang.visibility = View.GONE
            etFrameBarang.visibility = View.GONE
            etWarnaBarang.visibility = View.GONE
            etPasakBarang.visibility = View.GONE
            etCaraPemasangan.visibility = View.GONE
            etKegunaanBarang.visibility = View.VISIBLE
        }
    }

    private fun visibilityTenda() {
        with(binding) {
            etUkuranBarang.visibility = View.VISIBLE
            bahanLayout.visibility = View.GONE
            etTipeBarang.visibility = View.VISIBLE
            etFrameBarang.visibility = View.VISIBLE
            etWarnaBarang.visibility = View.GONE
            etPasakBarang.visibility = View.VISIBLE
            etCaraPemasangan.visibility = View.VISIBLE
            etKegunaanBarang.visibility = View.GONE
        }
    }

    private fun visibilitySleepingBag() {
        with(binding) {
            etUkuranBarang.visibility = View.VISIBLE
            bahanLayout.visibility = View.VISIBLE
            etTipeBarang.visibility = View.GONE
            etFrameBarang.visibility = View.GONE
            etWarnaBarang.visibility = View.GONE
            etPasakBarang.visibility = View.GONE
            etCaraPemasangan.visibility = View.GONE
            etKegunaanBarang.visibility = View.GONE
        }
    }

    private fun visibilitySepatuJaketAtauTas() {
        with(binding) {
            etUkuranBarang.visibility = View.VISIBLE
            bahanLayout.visibility = View.GONE
            etTipeBarang.visibility = View.GONE
            etFrameBarang.visibility = View.GONE
            etWarnaBarang.visibility = View.VISIBLE
            etPasakBarang.visibility = View.GONE
            etCaraPemasangan.visibility = View.GONE
            etKegunaanBarang.visibility = View.GONE
        }
    }

    private fun allClearRgJenisBarang() {
        with(binding) {
            etNamaBarang.text.clear()
            rgBahanBarang.clearCheck()
            etTipeBarang.text.clear()
            etUkuranBarang.text.clear()
            etFrameBarang.text.clear()
            etPasakBarang.text.clear()
            etWarnaBarang.text.clear()
            etStockBarang.text.clear()
            etHargaBarang.text.clear()
            etCaraPemasangan.text.clear()
            etKegunaanBarang.text.clear()
        }
    }

    private fun allClear(bind:FragmentTambahBarangBinding){
        with(bind){
            imgBarang.setImageResource(R.drawable.ic_add_photo)
            rgJenisBarang.clearCheck()
            etNamaBarang.text.clear()
            rgBahanBarang.clearCheck()
            etTipeBarang.text.clear()
            etUkuranBarang.text.clear()
            etFrameBarang.text.clear()
            etPasakBarang.text.clear()
            etWarnaBarang.text.clear()
            etStockBarang.text.clear()
            etHargaBarang.text.clear()
            etCaraPemasangan.text.clear()
            etKegunaanBarang.text.clear()
        }
    }

    private fun uploadData() {
        if (imageUri != null) {
            uploadToFirebase(imageUri)
        } else {
            Toast.makeText(activity, "Anda belum memilih gambarnya", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadToFirebase(uri: Uri?) {
        val id=databaseRef.push().key
        val fileRef =
            storageRef.child("${id}.jpg")
        if (uri != null && id !=null) {
            fileRef.putFile(uri).addOnSuccessListener {
                if (it.metadata != null && it.metadata?.reference != null) {
                    val result = it.storage.downloadUrl
                    result.addOnSuccessListener { uri ->
                        gambarUrl = uri.toString()
                        val model = Barang(
                            id,
                            jenisBarang,
                            namaBarang,
                            bahanBarang,
                            tipeBarang,
                            ukuranBarang,
                            frameBarang,
                            pasakBarang,
                            warnaBarang,
                            stockBarang.toInt(),
                            hargaBarang.toInt(),
                            caraPemasangan,
                            kegunaanBarang,
                            gambarUrl
                        )
                        databaseRef.child(id).setValue(model)
                        with(binding) {
                                Toast.makeText(activity, "Sukses mengupload", Toast.LENGTH_LONG)
                                    .show()
                            allClear(this)

                            rgJenisBarang.visibility = View.GONE
                            etNamaBarang.visibility = View.GONE
                            etUkuranBarang.visibility = View.GONE
                            etStockBarang.visibility = View.GONE
                            etHargaBarang.visibility = View.GONE
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

            binding.imgBarang.setImageURI(imageUri)
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
        binding.rgJenisBarang.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        if (imageUri != null) {
            binding.rgJenisBarang.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}