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
) : Parcelable {
    @Parcelize
    data class Keranjang(
        var idBarang: String = "",
        var namaBarang: String = "",
        var hargaBarang: Int = 0,
        var hari: Int = 0,
        var jumlah: Int = 0,
        var subtotal: Int = hargaBarang * hari * jumlah
    ) : Parcelable
}