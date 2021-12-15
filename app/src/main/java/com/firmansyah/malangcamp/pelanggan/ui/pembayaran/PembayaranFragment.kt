package com.firmansyah.malangcamp.pelanggan.ui.pembayaran

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firmansyah.malangcamp.adapter.KeranjangAdapter
import com.firmansyah.malangcamp.databinding.FragmentPembayaranBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.util.*

class PembayaranFragment : Fragment() {

    private lateinit var pembayaranViewModel: PembayaranViewModel
    private var _binding: FragmentPembayaranBinding? = null
    private lateinit var adapter: KeranjangAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var keranjangRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

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

        initAdapter()
        viewModel()
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
            val imageUri = data.data

            binding.imgBukti.setImageURI(imageUri)
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            binding.imgBukti.setImageBitmap(imageBitmap)
        }
    }

    private fun initAdapter() {
        adapter = KeranjangAdapter(arrayListOf())
        binding.rvListBarang.layoutManager = LinearLayoutManager(activity)
        binding.rvListBarang.adapter = adapter
    }

    private fun viewModel() {

        val currencyFormat= NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits=0
        currencyFormat.currency= Currency.getInstance("IDR")

        with(pembayaranViewModel) {
            getListBarang(keranjangRef)
            listBarang.observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter.setData(it)
                }

                var total=0

                for (i in it.indices){
                    total+=it[i].subtotal
                }

                binding.tvTotal.text=currencyFormat.format(total)
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