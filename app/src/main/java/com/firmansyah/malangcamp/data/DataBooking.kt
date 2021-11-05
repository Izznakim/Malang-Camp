package com.firmansyah.malangcamp.data

import com.firmansyah.malangcamp.model.Booking

object DataBooking {
    private val namaPengguna=arrayOf("A","B","C","D","E")

    private val namaPenyewa=arrayOf("Firman","Naufal","Firman","Zuddin","Firman")

    private val nomerTelp=arrayOf("085xxxxxx234","085xxxxxx754","085xxxxxx176","085xxxxxx786","085xxxxxx987")

    val listData:ArrayList<Booking>
    get(){
        val list= arrayListOf<Booking>()
        for (position in namaPengguna.indices){
            val booking=Booking()
            booking.namaPengguna= namaPengguna[position]
            booking.namaPenyewa= namaPenyewa[position]
            booking.nomerTelp= nomerTelp[position]
            list.add(booking)
        }
        return list
    }
}