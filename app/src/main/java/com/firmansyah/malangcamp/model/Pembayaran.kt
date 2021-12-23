package com.firmansyah.malangcamp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pembayaran(
    var idAkun: String = "",
    var idPembayaran: String = "",
    var tanggalPengambilan:String="",
    var hari: Int=0,
    var namaPenyewa: String = "",
    var noTelp: String = "",
    var buktiPembayaran: String = "",
    var total: Int = 0,
    var barangSewa: ArrayList<BarangSewa> = ArrayList()
) : Parcelable {
    @Parcelize
    data class BarangSewa(
        var idBarang: String = "",
        var namaBarang: String = "",
        var hargaBarang: Int = 0,
        var jumlah: Int = 0,
        var subtotal: Int = hargaBarang * jumlah
    ) : Parcelable
}
