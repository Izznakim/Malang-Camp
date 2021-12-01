package com.firmansyah.malangcamp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Barang(
    var jenis: String? = "",
    var nama: String? = "",
    var bahan: String? = "",
    var tipe: String? = "",
    var ukuran: String? = "",
    var frame: String? = "",
    var pasak: String? = "",
    var warna: String? = "",
    var caraPemasangan: String? = "",
    var gambar: String? = ""
) : Parcelable
