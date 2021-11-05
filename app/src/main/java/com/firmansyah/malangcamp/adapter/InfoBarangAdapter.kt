package com.firmansyah.malangcamp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.ListBookingBinding
import com.firmansyah.malangcamp.databinding.ListInfobarangBinding
import com.firmansyah.malangcamp.model.Tenda

class InfoBarangAdapter(private val listInfoBarang:ArrayList<Tenda>):RecyclerView.Adapter<InfoBarangAdapter.ListViewHolder>() {
    class ListViewHolder(private val binding: ListInfobarangBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(tenda: Tenda){
            with(binding){
                Glide.with(itemView.context)
                    .load(tenda.gambarTenda)
                    .apply(RequestOptions())
                    .into(gambarTenda)

                namaTenda.text=tenda.nama
                tipeTenda.text=itemView.context.getString(R.string.tipe_tenda,tenda.tipe)
                ukuranTenda.text=itemView.context.getString(R.string.ukuran_tenda,tenda.ukuran)
                frame.text=itemView.context.getString(R.string.frame_tenda,tenda.frame)
                pasak.text=itemView.context.getString(R.string.pasak_tenda,tenda.pasak)
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