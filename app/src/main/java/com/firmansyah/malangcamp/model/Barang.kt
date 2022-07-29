package com.firmansyah.malangcamp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Barang(
    var id: String = "",
    var jenis: String = "",
    var nama: String = "",
    var bahan: String = "",
    var tipe: String = "",
    var ukuran: String = "",
    var frame: String = "",
    var pasak: String = "",
    var warna: String = "",
    var stock: Int = 0,
    var harga: Int = 0,
    var rating: String = "",
    var caraPemasangan: String = "",
    var kegunaanBarang: String = "",
    var gambar: String = ""
) : Parcelable
