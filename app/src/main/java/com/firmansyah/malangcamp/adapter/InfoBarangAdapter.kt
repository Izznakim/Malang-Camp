package com.firmansyah.malangcamp.adapter

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.ListInfobarangBinding
import com.firmansyah.malangcamp.model.Barang

class InfoBarangAdapter(private val listInfoBarang: ArrayList<Barang>) :
    RecyclerView.Adapter<InfoBarangAdapter.ListViewHolder>() {
    fun setData(data: List<Barang>) {
        listInfoBarang.clear()
        listInfoBarang.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListInfobarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(barang.gambar)
                    .apply(RequestOptions())
                    .into(gambarBarang)

                tvNamaBarang.text = barang.nama
                tvTipeBarang.text = itemView.context.getString(R.string.tipe_tenda, barang.tipe)
                tvBahanBarang.text = itemView.context.getString(R.string.bahan_barang, barang.bahan)
                tvUkuranBarang.text =
                    itemView.context.getString(R.string.ukuran_barang, barang.ukuran)
                tvFrame.text = itemView.context.getString(R.string.frame_tenda, barang.frame)
                tvWarnaBarang.text = itemView.context.getString(R.string.warna_barang, barang.warna)
                tvPasak.text = itemView.context.getString(R.string.pasak_tenda, barang.pasak)
                when (barang.jenis) {
                    "Sepatu", "Jaket" -> {
                        tvBahanBarang.visibility = View.GONE
                        tvTipeBarang.visibility = View.GONE
                        tvFrame.visibility = View.GONE
                        tvWarnaBarang.visibility = View.VISIBLE
                        tvPasak.visibility = View.GONE
                        linearLayout2.visibility = View.GONE
                    }
                    "Sleeping Bag" -> {
                        tvBahanBarang.visibility = View.VISIBLE
                        tvTipeBarang.visibility = View.GONE
                        tvFrame.visibility = View.GONE
                        tvWarnaBarang.visibility = View.GONE
                        tvPasak.visibility = View.GONE
                        linearLayout2.visibility = View.GONE
                    }
                    "Tenda" -> {
                        tvBahanBarang.visibility = View.GONE
                        tvTipeBarang.visibility = View.VISIBLE
                        tvFrame.visibility = View.VISIBLE
                        tvWarnaBarang.visibility = View.GONE
                        tvPasak.visibility = View.VISIBLE
                        linearLayout2.visibility = View.VISIBLE
                        tvCaraPemasangan.visibility = View.VISIBLE

                        barang.caraPemasangan?.split("\n")?.forEachIndexed { index, value ->
                            @SuppressLint("SetTextI18n")
                            when {
                                index == 0 -> {
                                    tvCaraPemasangan.text = "\u2022 $value"
                                }
                                TextUtils.isEmpty(value.trim()) -> {
                                    tvCaraPemasangan.text = value
                                }
                                else -> {
                                    tvCaraPemasangan.append("\n\u2022 $value")
                                }
                            }
                        }
                    }
                    else -> {
                        gambarBarang.visibility = View.GONE
                        tvNamaBarang.visibility = View.GONE
                        tvBahanBarang.visibility = View.GONE
                        tvUkuranBarang.visibility = View.GONE
                        tvTipeBarang.visibility = View.GONE
                        tvFrame.visibility = View.GONE
                        tvWarnaBarang.visibility = View.GONE
                        tvPasak.visibility = View.GONE
                        linearLayout2.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ListInfobarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listInfoBarang[position])
    }

    override fun getItemCount(): Int = listInfoBarang.size
}