package com.firmansyah.malangcamp.adapter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.admin.ui.informasibarang.DetailInformasiFragment
import com.firmansyah.malangcamp.databinding.ListSewabarangBinding
import com.firmansyah.malangcamp.model.Barang


class BarangAdapter(
    private val listInfoBarang: ArrayList<Barang>,
    private val isAdmin: Boolean,
    private val onItemClicked: (Barang) -> Unit
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
            with(binding) {
                var stock = 0
                Glide.with(itemView.context)
                    .load(barang.gambar)
                    .apply(RequestOptions())
                    .into(gambarBarang)

                tvNamaBarang.text = barang.nama
                tvStockBarang.text = barang.stock.toString()
                tvHargaBarang.text = itemView.context.getString(R.string.rp, barang.harga)
                etStock.setText(stock.toString())

                if (isAdmin) {
                    deleteButton.visibility = View.VISIBLE
                    stockLayout.visibility = View.GONE
                } else {
                    deleteButton.visibility = View.GONE
                    stockLayout.visibility = View.VISIBLE
                }

                if (stockLayout.isVisible) {
                    etStock.doOnTextChanged { text, _, _, _ ->
                        try {
                            when {
                                text.isNullOrEmpty() -> stock = 0
                                text.toString().toInt() > barang.stock -> {
                                    stock = barang.stock
                                    etStock.setText(stock.toString())
                                }
                                else -> stock = text.toString().toInt()
                            }
                        } catch (e: NumberFormatException) {
                        }
                    }

                    btnDecrease.setOnClickListener {
                        stock--
                        if (stock < 1) {
                            stock = 0
                        }
                        etStock.setText(stock.toString())
                    }
                    btnIncrease.setOnClickListener {
                        stock++
                        if (stock > barang.stock) {
                            stock = barang.stock
                        }
                        etStock.setText(stock.toString())
                    }
                } else {
                    stock = 0
                }

                if (deleteButton.isVisible) {
                    deleteButton.setOnClickListener {
                        onItemClicked.invoke(barang)
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
                        Toast.makeText(itemView.context, stock.toString(), Toast.LENGTH_LONG).show()
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