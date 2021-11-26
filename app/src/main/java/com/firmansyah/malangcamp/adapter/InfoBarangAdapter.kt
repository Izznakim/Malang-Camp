package com.firmansyah.malangcamp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.ListInfobarangBinding
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pelanggan

class InfoBarangAdapter(private val listInfoBarang:ArrayList<Barang>):RecyclerView.Adapter<InfoBarangAdapter.ListViewHolder>() {
    fun setData(data:List<Barang>){
        listInfoBarang.clear()
        listInfoBarang.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListInfobarangBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang){
            with(binding){
                Glide.with(itemView.context)
                    .load(barang.gambar)
                    .apply(RequestOptions())
                    .into(gambarTenda)

                namaTenda.text=barang.nama
                tipeTenda.text=itemView.context.getString(R.string.tipe_tenda,barang.tipe)
                ukuranTenda.text=itemView.context.getString(R.string.ukuran_tenda,barang.ukuran)
                frame.text=itemView.context.getString(R.string.frame_tenda,barang.frame)
                pasak.text=itemView.context.getString(R.string.pasak_tenda,barang.pasak)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding=ListInfobarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listInfoBarang[position])
    }

    override fun getItemCount(): Int = listInfoBarang.size
}