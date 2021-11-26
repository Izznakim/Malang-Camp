package com.firmansyah.malangcamp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Barang(
    var nama: String? = "",
    var tipe: String? = "",
    var ukuran: String? = "",
    var frame: String? = "",
    var pasak: String? = "",
    var gambar: String? = ""
) : Parcelable
