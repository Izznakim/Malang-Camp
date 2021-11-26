package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.FragmentTambahBarangBinding
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class TambahBarangFragment : DialogFragment() {

    private var _binding: FragmentTambahBarangBinding? = null
    private var imageUri: Uri?=null
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef:StorageReference
    private lateinit var databaseRef:DatabaseReference

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

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("images/")

        database = FirebaseDatabase.getInstance()
        databaseRef=database.getReference("barang")

        buttonClick()
    }

    private fun buttonClick() {
        binding.imgBarang.setOnClickListener {
            selectImage()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.cancel()
        }

        binding.btnUpload.setOnClickListener {
            val namaBarang = binding.etNamaBarang.text.toString()
            val tipeBarang = binding.etTipeBarang.text.toString()
            val ukuranBarang = binding.etUkuranBarang.text.toString()
            val frameBarang = binding.etFrameBarang.text.toString()
            val pasakBarang = binding.etPasakBarang.text.toString()

            if (namaBarang.isEmpty()) {
                binding.etNamaBarang.error = "Nama harus diisi"
                binding.etNamaBarang.requestFocus()
                return@setOnClickListener
            }

            if (tipeBarang.isEmpty()) {
                binding.etTipeBarang.error = "Tipe harus diisi"
                binding.etTipeBarang.requestFocus()
                return@setOnClickListener
            }

            if (ukuranBarang.isEmpty()) {
                binding.etUkuranBarang.error = "Ukuran harus diisi"
                binding.etUkuranBarang.requestFocus()
                return@setOnClickListener
            }

            if (frameBarang.isEmpty()) {
                binding.etFrameBarang.error = "Frame harus diisi"
                binding.etFrameBarang.requestFocus()
                return@setOnClickListener
            }

            if (pasakBarang.isEmpty()) {
                binding.etPasakBarang.error = "Pasak harus diisi"
                binding.etPasakBarang.requestFocus()
                return@setOnClickListener
            }

            uploadData(namaBarang,tipeBarang,ukuranBarang,frameBarang,pasakBarang)
        }
    }

    private fun uploadData(namaBarang: String,tipeBarang:String,ukuranBarang:String,frameBarang:String,pasakBarang:String) {
        if (imageUri!=null){
            uploadToFirebase(namaBarang,tipeBarang,ukuranBarang,frameBarang,pasakBarang,imageUri)
        }else{
            Toast.makeText(activity, "Anda belum memilih gambarnya", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadToFirebase(namaBarang:String,tipeBarang:String,ukuranBarang:String,frameBarang:String,pasakBarang:String, uri:Uri?) {
        val fileRef=storageRef.child(namaBarang)
        if (uri != null) {
            fileRef.putFile(uri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    val model=Barang(namaBarang,tipeBarang,ukuranBarang,frameBarang,pasakBarang,uri.toString())
                    databaseRef.child(namaBarang).setValue(model)
                    Toast.makeText(activity, "Sukses mengupload", Toast.LENGTH_LONG).show()
                    with(binding){
                        imgBarang.setImageResource(R.drawable.ic_add_photo)
                        etNamaBarang.text.clear()
                        etTipeBarang.text.clear()
                        etUkuranBarang.text.clear()
                        etFrameBarang.text.clear()
                        etPasakBarang.text.clear()
                    }
                }.addOnFailureListener {
                    Toast.makeText(activity, "Gagal mengupload", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}