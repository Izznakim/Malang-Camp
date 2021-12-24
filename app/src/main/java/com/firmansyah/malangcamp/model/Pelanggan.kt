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
    var keranjangBarang: ArrayList<Keranjang> = ArrayList()
) : Parcelable