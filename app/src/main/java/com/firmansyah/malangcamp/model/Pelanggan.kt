package com.firmansyah.malangcamp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pelanggan(
    var id: String = "",
    var username: String = "",
    var email: String = "",
    var namaDepan: String = "",
    var namaBelakang: String = "",
    var noTelp: String = "",
    var password: String = "",
    var sewaBarang: ArrayList<Sewa> = ArrayList()
) : Parcelable

@Parcelize
data class Sewa(
    var idBarang: String = "",
    var jumlah: Int = 0
) : Parcelable