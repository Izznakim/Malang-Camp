package com.firmansyah.malangcamp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Keranjang(
    var idBarang: String = "",
    var namaBarang: String = "",
    var hargaBarang: Int = 0,
    var jumlah: Int = 0,
    var subtotal: Int = hargaBarang * jumlah
) : Parcelable
