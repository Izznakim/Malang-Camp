package com.firmansyah.malangcamp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.admin.ui.informasibarang.DetailInformasiFragment
import com.firmansyah.malangcamp.admin.ui.informasibarang.SubmitBarangFragment
import com.firmansyah.malangcamp.databinding.ListSewabarangBinding
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.model.Pelanggan


class InfoBarangAdapter(
    private val listInfoBarang: ArrayList<Barang>,
    private val onItemClicked: (Barang) -> Unit
) :
    RecyclerView.Adapter<InfoBarangAdapter.ListViewHolder>() {
    fun setData(data: List<Barang>) {
        listInfoBarang.clear()
        listInfoBarang.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ListSewabarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(barang: Barang) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(barang.gambar)
                    .apply(RequestOptions())
                    .into(gambarBarang)

                tvNamaBarang.text = barang.nama
                tvStockBarang.text= barang.stock.toString()
                tvHargaBarang.text=itemView.context.getString(R.string.rp, barang.harga)

                itemView.setOnClickListener {
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

                }

                deleteButton.setOnClickListener {
                    onItemClicked.invoke(barang)
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