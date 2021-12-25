package com.firmansyah.malangcamp.admin.ui.listbooking

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.adapter.KeranjangAdapter
import com.firmansyah.malangcamp.databinding.FragmentBookingDetailBinding
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class BookingDetailFragment : DialogFragment() {

    private lateinit var adapter: KeranjangAdapter

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
        _binding = FragmentBookingDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments!=null){
            pembayaran=arguments?.getParcelable(EXTRA_PEMBAYARAN)
            isAdmin=arguments?.getBoolean(EXTRA_ISADMIN,false)
        }

        Log.d("Admin ===> ",isAdmin.toString())

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

        adapter = KeranjangAdapter(listKeranjang)
        binding.rvListBarang.layoutManager = LinearLayoutManager(activity)
        binding.rvListBarang.adapter = adapter

        with(binding){
            if (isAdmin==true){
                btnTerima.visibility=View.VISIBLE
                btnTolak.visibility=View.VISIBLE
            }else{
                tvValidasi.visibility=View.VISIBLE
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
}