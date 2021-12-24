package com.firmansyah.malangcamp.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.admin.ui.listbooking.BookingDetailFragment
import com.firmansyah.malangcamp.databinding.ListBookingBinding
import com.firmansyah.malangcamp.model.Pembayaran

class BookingAdapter(
    private val listBooking: ArrayList<Pembayaran>
) : RecyclerView.Adapter<BookingAdapter.ListViewHolder>() {
    fun setData(data: List<Pembayaran>) {
        listBooking.clear()
        listBooking.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListBookingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(pembayaran: Pembayaran) {
            with(binding) {
                tvTanggal.text = "Pesanan ${pembayaran.tanggalPengambilan}"
                tvNamaPenyewa.text = pembayaran.namaPenyewa
                nomerTelpPenyewa.text = pembayaran.noTelp

                itemView.setOnClickListener {
                    val bookingDetailFragment = BookingDetailFragment()
                    val mFragmentManager =
                        (itemView.context as AppCompatActivity).supportFragmentManager
                    val bundle = Bundle()

                    bundle.putParcelable(BookingDetailFragment.EXTRA_PEMBAYARAN, pembayaran)
                    bookingDetailFragment.arguments = bundle
                    mFragmentManager.beginTransaction().apply {
                        replace(R.id.nav_host_fragment_activity_admin_home,bookingDetailFragment, BookingDetailFragment::class.java.simpleName)
                        addToBackStack(null)
                        commit()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listBooking[position])
    }

    override fun getItemCount(): Int = listBooking.size
}