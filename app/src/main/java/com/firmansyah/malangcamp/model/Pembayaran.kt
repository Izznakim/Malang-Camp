package com.firmansyah.malangcamp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pembayaran(
    var idAkun: String = "",
    var idPembayaran: String = "",
    var tanggalPengambilan:String="",
    var tanggalPengembalian:String="",
    var jamPengambilan:String="",
    var hari: Int=0,
    var namaPenyewa: String = "",
    var noTelp: String = "",
    var buktiPembayaran: String = "",
    var total: Int = 0,
    var status:String="",
    var barangSewa: ArrayList<Keranjang> = ArrayList()
) : Parcelable
