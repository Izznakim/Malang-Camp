package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.FragmentDetailInformasiBinding
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailInformasiFragment : DialogFragment(), View.OnClickListener {

    private var barang: Barang? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference

    private var imageUri: Uri? = null

    companion object {
        const val EXTRA_BARANG = "extra_barang"
    }

    private var _binding: FragmentDetailInformasiBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailInformasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("images/")

        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference("barang")

        if (arguments != null) {
            barang = arguments?.getParcelable(EXTRA_BARANG)
        }

        bnd()
        binding.imgBarang.setOnClickListener(this)
        binding.btnUpdate.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
    }

    private fun bnd() {
        with(binding) {
            Glide.with(this@DetailInformasiFragment)
                .load(barang?.gambar)
                .apply(RequestOptions())
                .into(imgBarang)
            etNamaBarang.setText(barang?.nama)
            tvJenisBarang.text = barang?.jenis
            etUkuranBarang.setText(barang?.ukuran)
            tvBahanBarang.text = barang?.bahan
            etTipeBarang.setText(barang?.tipe)
            etFrame.setText(barang?.frame)
            etPasak.setText(barang?.pasak)
            etWarnaBarang.setText(barang?.warna)
            etStockBarang.setText(barang?.stock.toString())
            etHargaBarang.setText(barang?.harga.toString())
            etCaraPemasangan.setText(barang?.caraPemasangan)

            when (barang?.jenis) {
                "Sepatu", "Jaket" -> {
                    etWarnaBarang.visibility = View.VISIBLE
                }
                "Sleeping Bag" -> {
                    tvBahanBarang.visibility = View.VISIBLE
                }
                "Tenda" -> {
                    etTipeBarang.visibility = View.VISIBLE
                    framePasakLayout.visibility = View.VISIBLE
                    tvCaraPemasangan.visibility = View.VISIBLE
                    etCaraPemasangan.visibility = View.VISIBLE
                }
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBarang -> {
                selectImage()
            }
            R.id.btnUpdate -> {
                barang?.let {
                    val uri: Uri? = imageUri
                    if (uri != null) {
                        withUpdateImage(uri, it)
                    } else {
                        withoutUpdateImage(it)
                    }
                }
            }
            R.id.btnCancel -> dialog?.cancel()
        }
    }

    private fun withoutUpdateImage(it: Barang) {
        val model =
            Barang(
                it.id,
                it.jenis,
                binding.etNamaBarang.text.toString(),
                it.bahan,
                binding.etTipeBarang.text.toString(),
                binding.etUkuranBarang.text.toString(),
                binding.etFrame.text.toString(),
                binding.etPasak.text.toString(),
                binding.etWarnaBarang.text.toString(),
                binding.etStockBarang.text.toString().toInt(),
                binding.etHargaBarang.text.toString().toInt(),
                binding.etCaraPemasangan.text.toString(),
                it.gambar
            )
        databaseRef.child(it.id).get().addOnSuccessListener { _ ->
            databaseRef.child(it.id).setValue(model)
            Toast.makeText(activity, "Sukses mengupdate", Toast.LENGTH_SHORT)
                .show()
            dialog?.dismiss()
        }.addOnFailureListener { e ->
            Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun withUpdateImage(uri: Uri, it: Barang) {
        val fileRef = storageRef.child("${binding.etNamaBarang.text}_(${it.jenis}).jpg")
        fileRef.putFile(uri).addOnSuccessListener { task ->
            if (task.metadata != null && task.metadata?.reference != null) {
                val result = task.storage.downloadUrl
                result.addOnSuccessListener { mUri ->
                    val model =
                        Barang(
                            it.id,
                            it.jenis,
                            binding.etNamaBarang.text.toString(),
                            it.bahan,
                            binding.etTipeBarang.text.toString(),
                            binding.etUkuranBarang.text.toString(),
                            binding.etFrame.text.toString(),
                            binding.etPasak.text.toString(),
                            binding.etWarnaBarang.text.toString(),
                            binding.etStockBarang.text.toString().toInt(),
                            binding.etHargaBarang.text.toString().toInt(),
                            binding.etCaraPemasangan.text.toString(),
                            mUri.toString()
                        )
                    databaseRef.child(it.id).get().addOnSuccessListener { _ ->
                        databaseRef.child(it.id).setValue(model)
                        Toast.makeText(activity, "Sukses mengupdate", Toast.LENGTH_SHORT)
                            .show()
                        dialog?.dismiss()
                    }.addOnFailureListener { e ->
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
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

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data

            binding.imgBarang.setImageURI(imageUri)
        }
    }
}
