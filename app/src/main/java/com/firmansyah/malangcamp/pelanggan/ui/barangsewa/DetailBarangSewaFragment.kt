package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.FragmentDetailBarangSewaBinding
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pelanggan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailBarangSewaFragment : DialogFragment(), View.OnClickListener {

    private lateinit var database: FirebaseDatabase
    private lateinit var userRef: DatabaseReference
    private lateinit var barangRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var barang: Barang? = null
    private var jumlah: Int? = 0

    companion object {
        const val EXTRA_BARANG = "extra_barang"
        const val EXTRA_JUMLAH = "extra_jumlah"
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
        userRef = database.getReference("users/${auth.currentUser?.uid}/keranjang")
        barangRef = database.getReference("barang")

        if (arguments != null) {
            barang = arguments?.getParcelable(EXTRA_BARANG)
            jumlah = arguments?.getInt(EXTRA_JUMLAH)
        }

        bnd()
        binding.btnTambah.setOnClickListener(this)
    }

    private fun bnd() {
        with(binding) {
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
            tvJumlahBarang.text = getString(R.string.jumlah___, jumlah, barang?.stock)
            tvHargaBarang.text = getString(R.string.rp, barang?.harga)

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

    override fun onClick(v: View?) {
        val idBarang = barang?.id
        val namaBarang=barang?.nama
        val hargaBarang=barang?.harga

        if (idBarang != null && namaBarang != null && hargaBarang != null && jumlah != 0 && jumlah != null) {
            var model: Pelanggan.Keranjang? =null
            jumlah?.let {
                model = Pelanggan.Keranjang(idBarang,namaBarang,hargaBarang, it,hargaBarang*it)
            }
            userRef.child(idBarang).get().addOnSuccessListener {
                userRef.child(idBarang).setValue(model)
                Toast.makeText(activity, "Telah ditambahkan di keranjang", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener { e ->
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (idBarang != null && jumlah == 0) {
            userRef.child(idBarang).get().addOnSuccessListener {
                it.ref.removeValue()
            }.addOnFailureListener {
                Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}