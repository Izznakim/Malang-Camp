package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.ZoomImageActivity
import com.firmansyah.malangcamp.databinding.FragmentDetailBarangSewaBinding
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Keranjang
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.util.*

class DetailBarangSewaFragment : DialogFragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var keranjangRef: DatabaseReference
    private lateinit var barangRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var barang: Barang? = null
    private var jumlah: Int = 0

    companion object {
        const val EXTRA_BARANG = "extra_barang"
    }

    private var _binding: FragmentDetailBarangSewaBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBarangSewaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        keranjangRef = database.getReference("users/${auth.currentUser?.uid}/keranjang")
        barangRef = database.getReference("barang")

        if (arguments != null) {
            barang = arguments?.getParcelable(EXTRA_BARANG)
        }

        bnd()
    }

    private fun bnd() {

        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 0
        currencyFormat.currency = Currency.getInstance("IDR")

        val stock=barang?.stock

        with(binding) {
            when (barang?.jenis) {
                "Sepatu", "Jaket", "Tas" -> {
                    tvUkuranBarang.visibility = View.VISIBLE
                    tvWarnaBarang.visibility = View.VISIBLE
                }
                "Sleeping Bag" -> {
                    tvUkuranBarang.visibility = View.VISIBLE
                    tvBahanBarang.visibility = View.VISIBLE
                }
                "Tenda" -> {
                    tvTipeBarang.visibility = View.VISIBLE
                    tvUkuranBarang.visibility = View.VISIBLE
                    framePasakLayout.visibility = View.VISIBLE
                    tvCaraPemasangan.visibility = View.VISIBLE
                }
                "Barang Lainnya" -> {
                    tvKegunaanBarang.visibility = View.VISIBLE
                }
            }

            Glide.with(this@DetailBarangSewaFragment)
                .load(barang?.gambar)
                .apply(RequestOptions())
                .into(imgBarang)
            tvNamaBarang.text = barang?.nama
            tvJenisBarang.text = barang?.jenis
            tvUkuranBarang.text = getString(R.string.ukuran___, barang?.ukuran)
            tvBahanBarang.text = getString(R.string.bahan___, barang?.bahan)
            tvTipeBarang.text = getString(R.string.tipe___, barang?.tipe)
            tvFrame.text = getString(R.string.frame___, barang?.frame)
            tvPasak.text = getString(R.string.pasak___, barang?.pasak)
            tvWarnaBarang.text = getString(R.string.warna___, barang?.warna)
            tvStockBarang.text = getString(R.string.stock___, stock)
            tvHargaBarang.text = currencyFormat.format(barang?.harga)

            barang?.caraPemasangan?.split("\n")?.forEachIndexed { index, value ->
                if (index == 0) {
                    tvCaraPemasangan.text = getString(R.string.cara_pemasangan___, "\u2022 $value")
                } else {
                    tvCaraPemasangan.append("\n\u2022 $value")
                }
            }

            barang?.kegunaanBarang?.split("\n")?.forEachIndexed { index, value ->
                if (index == 0) {
                    tvKegunaanBarang.text = getString(R.string.kegunaan_barang___, "\u2022 $value")
                } else {
                    tvKegunaanBarang.append("\n\u2022 $value")
                }
            }

            if (stock!=null) {
                etJumlah.doOnTextChanged { text, _, _, _ ->
                    try {
                        when {
                            text.isNullOrEmpty() -> jumlah = 0
                            text.toString().toInt() > stock -> {
                                jumlah = stock
                                etJumlah.setText(jumlah.toString())
                            }
                            else -> jumlah = text.toString().toInt()
                        }
                        if (jumlah==0){
                            btnTambah.isEnabled=false
                            btnTambah.setBackgroundColor(Color.GRAY)
                        }else{
                            btnTambah.isEnabled=true
                            btnTambah.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue))
                        }
                    } catch (e: NumberFormatException) {
                    }
                }

                imgBarang.setOnClickListener {
                    Intent(activity, ZoomImageActivity::class.java).also {
                        it.putExtra(ZoomImageActivity.EXTRA_IMAGE, barang?.gambar)
                        startActivity(it)
                    }
                }

                btnDecrease.setOnClickListener {
                    jumlah--
                    if (jumlah < 1) {
                        jumlah = 0
                    }
                    etJumlah.setText(jumlah.toString())
                }
                btnIncrease.setOnClickListener {
                    jumlah++
                    if (jumlah > stock) {
                        jumlah = stock
                    }
                    etJumlah.setText(jumlah.toString())
                }
            }

            if (jumlah==0){
                btnTambah.isEnabled=false
                btnTambah.setTextColor(Color.WHITE)
                btnTambah.setBackgroundColor(Color.GRAY)
            }else{
                btnTambah.isEnabled=true
                btnTambah.setTextColor(Color.WHITE)
                btnTambah.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue))
            }

            btnTambah.setOnClickListener {
                if (jumlah > 0) {
                    val model = barang?.let { barang ->
                        Keranjang(
                            barang.id,
                            barang.nama,
                            barang.harga,
                            jumlah,
                            barang.harga * jumlah
                        )
                    }
                    barang?.let { barang -> keranjangRef.child(barang.id).setValue(model) }

                    Toast.makeText(requireContext(),"${barang?.nama} telah ditambahkan ke keranjang",Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val idBarang=barang?.id
        if (idBarang!=null) {
            keranjangRef.child(idBarang).get().addOnSuccessListener {
                if (it.exists()){
                    val value=it.child("jumlah").value
                    if (binding.jumlahLayout.isVisible) {
                        binding.etJumlah.setText(value.toString())
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            }
        }

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }
}