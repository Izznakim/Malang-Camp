package com.firmansyah.malangcamp.adapter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.databinding.ListSewabarangBinding
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.pelanggan.ui.barangsewa.DetailBarangSewaFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.*


class BarangAdapter(
    private val listInfoBarang: ArrayList<Barang>,
    private val keranjangRef: DatabaseReference,
    private val isPegawai: Boolean,
    private val passData: (Barang) -> Unit
) :
    RecyclerView.Adapter<BarangAdapter.ListViewHolder>() {
    fun setData(data: List<Barang>) {
        listInfoBarang.clear()
        listInfoBarang.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListSewabarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang) {

            val currencyFormat = NumberFormat.getCurrencyInstance()
            currencyFormat.maximumFractionDigits = 0
            currencyFormat.currency = Currency.getInstance("IDR")

            val ratingArray=barang.rating.split(", ").toTypedArray()
            var sum = 0
            if (barang.rating.isEmpty()) {
                sum = 0
            } else {
                ratingArray.forEach {
                    sum += it.toInt()
                }
            }

            val rating:Double = sum.toDouble() / ratingArray.size.toDouble()

            with(binding) {

                if (isPegawai) {
                    deleteButton.visibility = View.VISIBLE
                } else {
                    deleteButton.visibility = View.GONE
                    keranjangRef.child(barang.id)
                        .addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val idBarang = snapshot.child("idBarang").value
                                    if (barang.id == idBarang) {
                                        cvSewaBarang.setCardBackgroundColor(Color.parseColor("#e6ffff"))
                                    }
                                } else {
                                    cvSewaBarang.setCardBackgroundColor(Color.WHITE)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(itemView.context, error.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                        })
                }

                Glide.with(itemView.context)
                    .load(barang.gambar)
                    .apply(RequestOptions())
                    .into(gambarBarang)

                tvNamaBarang.text = barang.nama
                tvStockBarang.text = barang.stock.toString()
                tvHargaBarang.text = currencyFormat.format(barang.harga)
                tvRate.text = itemView.context.getString(R.string.rate_5, rating)

                if (deleteButton.isVisible) {
                    deleteButton.setOnClickListener {
                        passData.invoke(barang)
                    }
                }

                itemView.setOnClickListener {
                    if (isPegawai) {
//                        val detailInformasiFragment = DetailInformasiFragment()
//                        val mFragmentManager =
//                            (itemView.context as AppCompatActivity).supportFragmentManager
//                        val bundle = Bundle()
//
//                        bundle.putParcelable(DetailInformasiFragment.EXTRA_BARANG, barang)
//                        detailInformasiFragment.show(
//                            mFragmentManager,
//                            DetailInformasiFragment::class.java.simpleName
//                        )
//                        detailInformasiFragment.arguments = bundle
                    } else {
                        val detailBarangSewa = DetailBarangSewaFragment()
                        val mFragmentManager =
                            (itemView.context as AppCompatActivity).supportFragmentManager
                        val bundle = Bundle()

                        bundle.putParcelable(DetailBarangSewaFragment.EXTRA_BARANG, barang)
                        detailBarangSewa.show(
                            mFragmentManager,
                            DetailBarangSewaFragment::class.java.simpleName
                        )
                        detailBarangSewa.arguments = bundle
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ListSewabarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listInfoBarang[position])
    }

    override fun getItemCount(): Int = listInfoBarang.size
}