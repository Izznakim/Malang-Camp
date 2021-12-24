package com.firmansyah.malangcamp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firmansyah.malangcamp.databinding.ListKeranjangBinding
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pelanggan
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class KeranjangAdapter(private val listBarang: ArrayList<Keranjang>):RecyclerView.Adapter<KeranjangAdapter.ListViewHolder>() {
    fun setData(data: List<Keranjang>) {
        listBarang.clear()
        listBarang.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListKeranjangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(barang:Keranjang){

            val currencyFormat= NumberFormat.getCurrencyInstance()
            currencyFormat.maximumFractionDigits=0
            currencyFormat.currency= Currency.getInstance("IDR")

            with(binding){
                tvNamaBarangSewa.text = barang.namaBarang
                tvHarga.text = currencyFormat.format(barang.hargaBarang)
                tvJumlah.text = "(${barang.jumlah})"
                tvSubtotal.text =
                    currencyFormat.format(barang.hargaBarang * barang.jumlah)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListKeranjangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listBarang[position])
    }

    override fun getItemCount(): Int = listBarang.size
}