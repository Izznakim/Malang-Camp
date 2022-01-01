package com.firmansyah.malangcamp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.admin.ui.informasibarang.DetailInformasiFragment
import com.firmansyah.malangcamp.databinding.ListSewabarangBinding
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.pelanggan.ui.barangsewa.DetailBarangSewaFragment
import java.text.NumberFormat
import java.util.*


class BarangAdapter(
    private val listInfoBarang: ArrayList<Barang>,
    private val isAdmin: Boolean,
    private val passData: (Barang, Int) -> Unit
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

            with(binding) {
                var jumlah = 0
                Glide.with(itemView.context)
                    .load(barang.gambar)
                    .apply(RequestOptions())
                    .into(gambarBarang)

                tvNamaBarang.text = barang.nama
                tvStockBarang.text = barang.stock.toString()
                tvHargaBarang.text = currencyFormat.format(barang.harga)
                etJumlah.setText(jumlah.toString())

                if (isAdmin) {
                    deleteButton.visibility = View.VISIBLE
                    jumlahLayout.visibility = View.GONE
                } else {
                    deleteButton.visibility = View.GONE
                    jumlahLayout.visibility = View.VISIBLE
                }

                if (jumlahLayout.isVisible) {
                    etJumlah.doOnTextChanged { text, _, _, _ ->
                        try {
                            when {
                                text.isNullOrEmpty() -> jumlah = 0
                                text.toString().toInt() > barang.stock -> {
                                    jumlah = barang.stock
                                    etJumlah.setText(jumlah.toString())
                                }
                                else -> jumlah = text.toString().toInt()
                            }
                            passData.invoke(barang, jumlah)
                        } catch (e: NumberFormatException) {
                        }
                    }

                    btnDecrease.setOnClickListener {
                        jumlah--
                        if (jumlah < 1) {
                            jumlah = 0
                        }
                        etJumlah.setText(jumlah.toString())
                    }
                    btnIncrease.setOnClickListener {
                        jumlah++
                        if (jumlah > barang.stock) {
                            jumlah = barang.stock
                        }
                        etJumlah.setText(jumlah.toString())
                    }
                } else {
                    jumlah = 0
                }

                if (deleteButton.isVisible) {
                    deleteButton.setOnClickListener {
                        passData.invoke(barang, 0)
                    }
                }

                itemView.setOnClickListener {
                    if (isAdmin) {
                        val detailInformasiFragment = DetailInformasiFragment()
                        val mFragmentManager =
                            (itemView.context as AppCompatActivity).supportFragmentManager
                        val bundle = Bundle()

                        bundle.putParcelable(DetailInformasiFragment.EXTRA_BARANG, barang)
                        detailInformasiFragment.show(
                            mFragmentManager,
                            DetailInformasiFragment::class.java.simpleName
                        )
                        detailInformasiFragment.arguments = bundle
                    } else {
                        val detailBarangSewa = DetailBarangSewaFragment()
                        val mFragmentManager =
                            (itemView.context as AppCompatActivity).supportFragmentManager
                        val bundle = Bundle()

                        bundle.putParcelable(DetailBarangSewaFragment.EXTRA_BARANG, barang)
                        bundle.putInt(DetailBarangSewaFragment.EXTRA_JUMLAH, jumlah)
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