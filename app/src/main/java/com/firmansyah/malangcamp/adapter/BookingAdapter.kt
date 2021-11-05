package com.firmansyah.malangcamp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firmansyah.malangcamp.databinding.ListBookingBinding
import com.firmansyah.malangcamp.model.Booking

class BookingAdapter(private val listBooking: ArrayList<Booking>):RecyclerView.Adapter<BookingAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding:ListBookingBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(booking:Booking){
            with(binding){
                namaPengguna.text=booking.namaPengguna
                namaPenyewa.text=booking.namaPenyewa
                nomerTelpPenyewa.text=booking.nomerTelp
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding=ListBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listBooking[position])
    }

    override fun getItemCount(): Int  = listBooking.size
}