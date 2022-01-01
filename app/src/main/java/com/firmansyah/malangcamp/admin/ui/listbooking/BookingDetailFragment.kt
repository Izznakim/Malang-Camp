package com.firmansyah.malangcamp.admin.ui.listbooking

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.adapter.KeranjangAdapter
import com.firmansyah.malangcamp.databinding.FragmentBookingDetailBinding
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.NumberFormat
import java.util.*

class BookingDetailFragment : DialogFragment() {

    private lateinit var adapter: KeranjangAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var pembayaranRef: DatabaseReference
    private lateinit var barangRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageBuktiRef: StorageReference

    private var pembayaran: Pembayaran?=null
    private var isAdmin:Boolean? = false
    private val listKeranjang:ArrayList<Keranjang> = arrayListOf()
    private var _binding: FragmentBookingDetailBinding? = null

    private val binding get() = _binding!!

    companion object{
        const val EXTRA_PEMBAYARAN="extra_pembayaran"
        const val EXTRA_ISADMIN="extra_isAdmin"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        database = Firebase.database
        pembayaranRef = database.getReference("pembayaran")
        barangRef = database.getReference("barang")

        storage = FirebaseStorage.getInstance()
        storageBuktiRef = storage.getReference("bukti/")

        _binding = FragmentBookingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments!=null){
            pembayaran=arguments?.getParcelable(EXTRA_PEMBAYARAN)
            isAdmin=arguments?.getBoolean(EXTRA_ISADMIN,false)
        }

        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 0
        currencyFormat.currency = Currency.getInstance("IDR")

        val barangSewa=pembayaran?.barangSewa
        if (barangSewa?.indices!=null) {
            for (i in barangSewa.indices){
                val keranjang=Keranjang()
                keranjang.idBarang = barangSewa[i].idBarang
                keranjang.namaBarang = barangSewa[i].namaBarang
                keranjang.hargaBarang = barangSewa[i].hargaBarang
                keranjang.jumlah = barangSewa[i].jumlah
                keranjang.subtotal = barangSewa[i].subtotal
                listKeranjang.add(keranjang)
            }
        }

        initAdapter()
        bnd(currencyFormat,barangSewa)
    }

    @SuppressLint("SetTextI18n")
    private fun bnd(currencyFormat: NumberFormat, barangSewa: ArrayList<Keranjang>?) {
        with(binding){
            if (isAdmin==true){
                btnTerima.visibility=View.VISIBLE
                btnTolak.visibility=View.VISIBLE
            }else{
                llValidasi.visibility=View.VISIBLE
            }

            tvTgl.text=pembayaran?.tanggalPengambilan
            tvHari.text= "Selama ${pembayaran?.hari.toString()} Hari"
            tvTotal.text=currencyFormat.format(pembayaran?.total)
            tvNamaPenyewa.text=pembayaran?.namaPenyewa
            tvNoTelp.text=pembayaran?.noTelp
            Glide.with(requireContext())
                .load(pembayaran?.buktiPembayaran)
                .apply(RequestOptions())
                .into(imgBukti)

            val idPembayaran=pembayaran?.idPembayaran
            when{
                btnTerima.isVisible && btnTolak.isVisible->{
                    if (idPembayaran != null) {
                        btnTerima.setOnClickListener {
                            pembayaranRef.child(idPembayaran).child("status").setValue("diterima")
                            Toast.makeText(activity, "Penyewaan telah diTERIMA", Toast.LENGTH_LONG).show()
                            dialog?.dismiss()
                        }
                        btnTolak.setOnClickListener {
                            pembayaranRef.child(idPembayaran).child("status").setValue("ditolak")
                            if (barangSewa?.indices!=null) {
                                for (i in barangSewa.indices){
                                    barangRef.child(barangSewa[i].idBarang).child("stock").get().addOnSuccessListener {
                                        val value=it.getValue<Int>()
                                        if (value != null) {
                                            barangRef.child(barangSewa[i].idBarang).child("stock").setValue(value+barangSewa[i].jumlah)
                                        }
                                    }.addOnFailureListener {
                                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            Toast.makeText(activity, "Penyewaan telah diTOLAK", Toast.LENGTH_LONG).show()
                            dialog?.dismiss()
                        }
                    }
                }
                llValidasi.isVisible->{
                    val status=pembayaran?.status
                    if (status!=null){
                        when(status){
                            "diterima"-> {
                                tvValidasi.text =
                                    "PESANAN DI TERIMA. SILAHKAN UNTUK MENGAMBIL BARANG SESUAI JADWAL YANG SUDAH DI PESAN."
                                tvValidasi.setTextColor(Color.parseColor("#43a047"))
                                tvValidasi.typeface = Typeface.DEFAULT_BOLD
                                btnHapus.text="Hapus pesanan"
                            }
                            "ditolak"-> {
                                tvValidasi.text =
                                    "MAAF, PESANAN ANDA TIDAK BISA KAMI PROSES KARENA TIDAK VALID"
                                tvValidasi.setTextColor(Color.parseColor("#FF0A0A"))
                                tvValidasi.typeface = Typeface.DEFAULT_BOLD
                                btnHapus.text="Hapus pesanan"
                            }
                            "netral"-> {
                                tvValidasi.text =
                                    "MAAF, PESANAN ANDA BELUM KAMI KONFIRMASI. DIMOHON UNTUK MENUNGGU BEBERAPA SAAT LAGI."
                                btnHapus.text="Batalkan pemesanan"
                            }
                        }

                        btnHapus.setOnClickListener {
                            if (idPembayaran != null) {
                                pembayaranRef.child(idPembayaran).get().addOnSuccessListener {
                                    if (btnHapus.text == "Batalkan pemesanan") {
                                        if (barangSewa?.indices!=null) {
                                            for (i in barangSewa.indices){
                                                barangRef.child(barangSewa[i].idBarang).child("stock").get().addOnSuccessListener { snapshot ->
                                                    val value=snapshot.getValue<Int>()
                                                    if (value != null) {
                                                        barangRef.child(barangSewa[i].idBarang).child("stock").setValue(value+barangSewa[i].jumlah)
                                                    }
                                                }.addOnFailureListener { e ->
                                                    Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        }
                                    }

                                    it.ref.removeValue()
                                    storageBuktiRef.child("${idPembayaran}.jpg").delete()
                                }.addOnFailureListener {
                                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                                }
                            }
                            dialog?.dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = KeranjangAdapter(listKeranjang)
        binding.rvListBarang.layoutManager = LinearLayoutManager(activity)
        binding.rvListBarang.adapter = adapter
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
}