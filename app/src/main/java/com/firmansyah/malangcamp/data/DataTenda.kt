package com.firmansyah.malangcamp.data

import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Tenda

object DataTenda {
    private val namaTenda = arrayOf(
        "Tenda Quechue 2 Orang",
        "TENDA GREAT OUTDOOR KAP 3-4 ORG",
        "TENDA GREAT OUTDOOR KAP 8 ORG",
        "Tenda Rei 02 Kapasitas 2 Orang",
        "TENDA Consina HIKER 4 ORG",
        "TENDA RIGI KAP 4-5 ORG",
        "TENDA BESTWAY KAP 4 ORG"
    )

    private val tipeTenda = arrayOf(
        "Tenda Double Layer",
        "Tenda Double Layer",
        "Tenda Double Layer",
        "Single Layer",
        "Tenda Double Layer",
        "Tenda Double Layer",
        "Tenda Double Layer",
    )

    private val ukuranTenda = arrayOf(
        "130cm x 210cm",
        "200cm x 200cm",
        "300cm x 300cm",
        "200cm x 120cm x 100cm",
        "202cm x 202cm",
        "240cm x 210cm",
        "220cm x 210cm"
    )

    private val frameTenda = arrayOf(
        "2",
        "3 (2 panjang, 1 pendek)",
        "3 (2 panjang, 1 pendek)",
        "2",
        "2",
        "3 (2 panjang, 1 pendek)",
        "3 (2 panjang, 1 pendek)"
    )

    private val pasakTenda = arrayOf(
        "8", "8", "10", "8", "8", "8", "8"
    )

    private val gambarTenda = intArrayOf(
        R.drawable.tenda_1,
        R.drawable.tenda_2,
        R.drawable.tenda_3,
        R.drawable.tenda_4,
        R.drawable.tenda_5,
        R.drawable.tenda_6,
        R.drawable.tenda_7
    )

    val listData:ArrayList<Tenda>
    get(){
        val list= arrayListOf<Tenda>()
        for (position in namaTenda.indices){
            val tenda=Tenda()
            tenda.nama= namaTenda[position]
            tenda.tipe= tipeTenda[position]
            tenda.ukuran= ukuranTenda[position]
            tenda.frame= frameTenda[position]
            tenda.pasak= pasakTenda[position]
            tenda.gambarTenda= gambarTenda[position]
            list.add(tenda)
        }
        return list
    }
}