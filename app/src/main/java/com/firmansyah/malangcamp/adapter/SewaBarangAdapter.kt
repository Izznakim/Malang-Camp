package com.firmansyah.malangcamp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.ListInfobarangBinding
import com.firmansyah.malangcamp.databinding.ListSewabarangBinding
import com.firmansyah.malangcamp.model.Barang

class SewaBarangAdapter(private val listSewaBarang: ArrayList<Barang>) :
    RecyclerView.Adapter<SewaBarangAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ListSewabarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(barang.gambarBarang)
                    .apply(RequestOptions())
                    .into(gambarBarang)

                namaBarang.text = barang.nama
                stockBarang.text = barang.stock.toString()
                hargaBarang.text = itemView.context.getString(R.string.rp, barang.harga)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding=
            ListSewabarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listSewaBarang[position])
    }

    override fun getItemCount(): Int = listSewaBarang.size
}