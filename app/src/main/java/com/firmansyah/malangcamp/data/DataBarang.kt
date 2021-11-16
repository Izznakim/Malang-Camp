package com.firmansyah.malangcamp.data

import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Barang

object DataBarang {
    private val namaBarang =
        arrayOf(
            "Sleeping Bag",
            "Jaket Biru",
            "Kompor",
            "Sepatu Ukurang 39 CSN",
            "Tas Carrier Cozmed",
            "Tenda Quechue 2 Orang"
        )

    private val stockBarang = intArrayOf(5, 3, 7, 2, 10, 8)

    private val hargaBarang = intArrayOf(10000, 14000, 5000, 9000, 10000, 19000)

    private val gambarBarang = intArrayOf(
        R.drawable.sleeping_bag,
        R.drawable.jaket_biru,
        R.drawable.kompor,
        R.drawable.sepatu_ukurang_39_csn,
        R.drawable.tas_carrier_cozmed,
        R.drawable.tenda_1
    )

    val listData: ArrayList<Barang>
        get() {
            val list = arrayListOf<Barang>()
            for (position in namaBarang.indices) {
                val barang = Barang()
                barang.nama = namaBarang[position]
                barang.stock = stockBarang[position]
                barang.harga = hargaBarang[position]
                barang.gambarBarang = gambarBarang[position]
                list.add(barang)
            }
            return list
        }
}