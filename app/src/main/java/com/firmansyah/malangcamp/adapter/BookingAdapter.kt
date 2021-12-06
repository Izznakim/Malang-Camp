package com.firmansyah.malangcamp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.ListBookingBinding
import com.firmansyah.malangcamp.model.Pelanggan

class BookingAdapter(
    private val listPelanggan: ArrayList<Pelanggan>,
    private val onItemClicked: (Pelanggan) -> Unit
) : RecyclerView.Adapter<BookingAdapter.ListViewHolder>() {
    fun setData(data: List<Pelanggan>) {
        listPelanggan.clear()
        listPelanggan.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Pelanggan) {
            with(binding) {
                namaPengguna.text = user.username
                namaPenyewa.text = itemView.context.getString(
                    R.string.nama_penyewa,
                    user.namaDepan,
                    user.namaBelakang
                )
                nomerTelpPenyewa.text = user.noTelp

                deleteButton.setOnClickListener {
                    onItemClicked.invoke(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPelanggan[position])
    }

    override fun getItemCount(): Int  = listPelanggan.size
}