package com.firmansyah.malangcamp.admin.ui.listbooking

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.firmansyah.malangcamp.other.ZoomImageActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.NumberFormat
import java.util.*
import android.R.attr.text

import android.R
import android.R.attr
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.content.ContextCompat


class BookingDetailFragment : DialogFragment() {

    private lateinit var adapter: KeranjangAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var pembayaranRef: DatabaseReference
    private lateinit var barangRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageBuktiRef: StorageReference

    private var pembayaran: Pembayaran? = null
    private var isAdmin: Boolean? = false
    private val listKeranjang: ArrayList<Keranjang> = arrayListOf()
    private var _binding: FragmentBookingDetailBinding? = null

    private val binding get() = _binding!!

    companion object {
        const val EXTRA_PEMBAYARAN = "extra_pembayaran"
        const val EXTRA_ISADMIN = "extra_isAdmin"
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

        if (arguments != null) {
            pembayaran = arguments?.getParcelable(EXTRA_PEMBAYARAN)
            isAdmin = arguments?.getBoolean(EXTRA_ISADMIN, false)
        }

        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.maximumFractionDigits = 0
        currencyFormat.currency = Currency.getInstance("IDR")

        val barangSewa = pembayaran?.barangSewa
        if (barangSewa?.indices != null) {
            for (i in barangSewa.indices) {
                val keranjang = Keranjang()
                keranjang.idBarang = barangSewa[i].idBarang
                keranjang.namaBarang = barangSewa[i].namaBarang
                keranjang.hargaBarang = barangSewa[i].hargaBarang
                keranjang.jumlah = barangSewa[i].jumlah
                keranjang.subtotal = barangSewa[i].subtotal
                listKeranjang.add(keranjang)
            }
        }

        initAdapter()
        bnd(currencyFormat, barangSewa)
    }

    @SuppressLint("SetTextI18n")
    private fun bnd(currencyFormat: NumberFormat, barangSewa: ArrayList<Keranjang>?) {
        with(binding) {
            if (isAdmin == true) {
                btnTerima.visibility = View.VISIBLE
                btnTolak.visibility = View.VISIBLE
            } else {
                llValidasi.visibility = View.VISIBLE
            }

            val textTglAmbil=pembayaran?.tanggalPengambilan
            val textTglKembali=pembayaran?.tanggalPengembalian
            val textJam=pembayaran?.jamPengambilan
            val textKet="Barang diambil tanggal: $textTglAmbil dan dikembalikan tanggal: $textTglKembali, pada jam $textJam"

            val sb=SpannableStringBuilder(textKet)
            val fcs1=ForegroundColorSpan(Color.parseColor("#009903"))
            val fcs2=ForegroundColorSpan(Color.parseColor("#990003"))
            val fcs3=ForegroundColorSpan(
                ContextCompat.getColor(requireContext(),
                    com.firmansyah.malangcamp.R.color.blue_dark))


            if (textTglAmbil!=null && textTglKembali!=null && textJam!=null) {
                sb.setSpan(fcs1, textKet.indexOf(textTglAmbil), textKet.indexOf(textTglAmbil)+textTglAmbil.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

                sb.setSpan(fcs2, textKet.indexOf(textTglKembali), textKet.indexOf(textTglKembali)+textTglKembali.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

                sb.setSpan(fcs3, textKet.indexOf(textJam), textKet.indexOf(textJam)+textJam.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }

            tvTgl.text=sb
            tvHari.text = "Selama ${pembayaran?.hari.toString()} Hari"
            tvTotal.text = currencyFormat.format(pembayaran?.total)
            tvNamaPenyewa.text = pembayaran?.namaPenyewa
            tvNoTelp.text = pembayaran?.noTelp
            Glide.with(requireContext())
                .load(pembayaran?.buktiPembayaran)
                .apply(RequestOptions())
                .into(imgBukti)
            imgBukti.setOnClickListener {
                Intent(activity, ZoomImageActivity::class.java).also {
                    it.putExtra(ZoomImageActivity.EXTRA_IMAGE, pembayaran?.buktiPembayaran)
                    startActivity(it)
                }
            }

            val idPembayaran = pembayaran?.idPembayaran
            when {
                btnTerima.isVisible && btnTolak.isVisible -> {
                    if (idPembayaran != null) {
                        btnTerima.setOnClickListener {
                            pembayaranRef.child(idPembayaran).child("status").setValue("diterima")
                            Toast.makeText(activity, "Penyewaan telah diTERIMA", Toast.LENGTH_LONG)
                                .show()
                            dialog?.dismiss()
                        }
                        btnTolak.setOnClickListener {
                            pembayaranRef.child(idPembayaran).child("status").setValue("ditolak")
                            if (barangSewa?.indices != null) {
                                for (i in barangSewa.indices) {
                                    barangRef.child(barangSewa[i].idBarang).child("stock").get()
                                        .addOnSuccessListener {
                                            val value = it.getValue<Int>()
                                            if (value != null) {
                                                barangRef.child(barangSewa[i].idBarang)
                                                    .child("stock")
                                                    .setValue(value + barangSewa[i].jumlah)
                                            }
                                        }.addOnFailureListener {
                                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG)
                                            .show()
                                    }
                                }
                            }
                            Toast.makeText(activity, "Penyewaan telah diTOLAK", Toast.LENGTH_LONG)
                                .show()
                            dialog?.dismiss()
                        }
                    }
                }
                llValidasi.isVisible -> {
                    val status = pembayaran?.status
                    if (status != null) {
                        when (status) {
                            "diterima" -> {
                                tvValidasi.text =
                                    "pesanan anda di terima. silakan mengambil barang sesuai dengan tanggal dan jam yang di pesan. tetap memakai masker pada saat pengambilan barang.\nHubungi nomor di bawah ini untuk informasi lebih lanjut."
                                tvValidasi.setTextColor(Color.parseColor("#43a047"))
                                tvValidasi.typeface = Typeface.DEFAULT_BOLD
                                etNomorWA.setOnClickListener {
                                    val phone = etNomorWA.text
                                    intentToWhatsApp(phone)
                                }
                                btnHapus.text = "Hapus pesanan"
                            }
                            "ditolak" -> {
                                tvValidasi.text =
                                    "MAAF, PESANAN ANDA TIDAK BISA KAMI PROSES KARENA TIDAK VALID.\nHubungi nomor di bawah ini untuk informasi lebih lanjut."
                                tvValidasi.setTextColor(Color.parseColor("#FF0A0A"))
                                tvValidasi.typeface = Typeface.DEFAULT_BOLD
                                etNomorWA.setOnClickListener {
                                    val phone = etNomorWA.text
                                    intentToWhatsApp(phone)
                                }
                                btnHapus.text = "Hapus pesanan"
                            }
                            "netral" -> {
                                tvValidasi.text =
                                    "MAAF, PESANAN ANDA BELUM KAMI KONFIRMASI. DIMOHON UNTUK MENUNGGU BEBERAPA SAAT LAGI.\nHubungi nomor di bawah ini untuk informasi lebih lanjut."
                                etNomorWA.setOnClickListener {
                                    val phone = etNomorWA.text
                                    intentToWhatsApp(phone)
                                }
                                btnHapus.text = "Batalkan pemesanan"
                            }
                        }

                        btnHapus.setOnClickListener {
                            if (idPembayaran != null) {
                                pembayaranRef.child(idPembayaran).get().addOnSuccessListener {
                                    if (btnHapus.text == "Batalkan pemesanan") {
                                        if (barangSewa?.indices != null) {
                                            for (i in barangSewa.indices) {
                                                barangRef.child(barangSewa[i].idBarang)
                                                    .child("stock").get()
                                                    .addOnSuccessListener { snapshot ->
                                                        val value = snapshot.getValue<Int>()
                                                        if (value != null) {
                                                            barangRef.child(barangSewa[i].idBarang)
                                                                .child("stock")
                                                                .setValue(value + barangSewa[i].jumlah)
                                                        }
                                                    }.addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        activity,
                                                        e.message,
                                                        Toast.LENGTH_LONG
                                                    ).show()
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

    private fun intentToWhatsApp(phone: Editable) {
        val packageManager = context?.packageManager
        val intent = Intent(Intent.ACTION_VIEW)

        if (packageManager != null) {
            try {
                val url = "https://api.whatsapp.com/send?phone=$phone"
                intent.setPackage("com.whatsapp")
                intent.data = Uri.parse(url)
                if (intent.resolveActivity(packageManager) != null) {
                    context?.startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initAdapter() {
        adapter = KeranjangAdapter(listKeranjang, barangRef, true)
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