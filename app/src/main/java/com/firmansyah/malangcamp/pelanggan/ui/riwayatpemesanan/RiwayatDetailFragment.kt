package com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.model.Pembayaran
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BUKTI_LOCATION
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DITERIMA
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DITOLAK
import com.firmansyah.malangcamp.other.ConstVariable.Companion.EXTRA_BARANG
import com.firmansyah.malangcamp.other.ConstVariable.Companion.EXTRA_PEMBAYARAN
import com.firmansyah.malangcamp.other.ConstVariable.Companion.NETRAL
import com.firmansyah.malangcamp.other.ConstVariable.Companion.PEMBAYARAN
import com.firmansyah.malangcamp.other.ConstVariable.Companion.STOCK_PATH
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.firmansyah.malangcamp.theme.black
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

//  Halaman detail informasi pelanggan yang memesan
class RiwayatDetailFragment : DialogFragment() {

    private lateinit var riwayatDetailViewModel: RiwayatDetailViewModel
    private lateinit var database: FirebaseDatabase
    private lateinit var pembayaranRef: DatabaseReference
    private lateinit var barangRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageBuktiRef: StorageReference

    private var pembayaran: Pembayaran? = null
    private val listKeranjang: ArrayList<Keranjang> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        riwayatDetailViewModel = ViewModelProvider(this)[RiwayatDetailViewModel::class.java]
        // Inflate the layout for this fragment

        database = Firebase.database
        pembayaranRef = database.getReference(PEMBAYARAN)
        barangRef = database.getReference(BARANG)

        storage = FirebaseStorage.getInstance()
        storageBuktiRef = storage.getReference(BUKTI_LOCATION)

        return ComposeView(requireContext()).apply {
            setContent {
                riwayatDetailViewModel.getPembayaran(pembayaran)
                val mPembayaran = riwayatDetailViewModel.pembayaran
                val hapusText = when (mPembayaran.status) {
                    NETRAL -> context.getString(R.string.batalkan_pemesanan)
                    else -> context.getString(R.string.hapus_pesanan)
                }

                MalangCampTheme {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                            .fillMaxSize()
                    ) {
                        item { ListBarangTitle() }
                        item { TglSerahTerima(mPembayaran) }
                        item {
                            Divider(
                                thickness = 1.dp,
                                color = black,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                        listKeranjang(listKeranjang)
                        item { Hari(mPembayaran) }
                        item { Total(currencyIdrFormat().format(mPembayaran.total)) }
                        item { NamaPenyewa(mPembayaran) }
                        item { PhoneNumber(requireContext(), mPembayaran) }
                        item { BuktiPembayaranImage(mPembayaran, requireContext()) }
                        item {
                            Text(
                                text = when (mPembayaran.status) {
                                    DITERIMA -> context.getString(R.string.pesanan_anda_di_terima)
                                        .uppercase(
                                            Locale.getDefault()
                                        )
                                    DITOLAK -> context.getString(R.string.pesanan_anda_tidak_bisa_kami_proses)
                                        .uppercase(
                                            Locale.getDefault()
                                        )
                                    else -> context.getString(R.string.pesanan_anda_belum_kami_konfirmasi)
                                        .uppercase(
                                            Locale.getDefault()
                                        )
                                },
                                color = when (mPembayaran.status) {
                                    DITERIMA -> Color.Green
                                    DITOLAK -> Color.Red
                                    else -> Color.Unspecified
                                },
                                fontWeight = when (mPembayaran.status) {
                                    DITERIMA -> FontWeight.Bold
                                    DITOLAK -> FontWeight.Bold
                                    else -> null
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        item {
                            Text(text = stringResource(id = R.string.NmerTlpnPegawai),
                                color = Color.Blue,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        intentToWhatsApp(requireContext().getString(R.string.NmerTlpnPegawai))
                                    })
                        }
                        item {
                            Button(
                                onClick = {
                                    pembayaranRef.child(mPembayaran.idPembayaran).get()
                                        .addOnSuccessListener {
                                            if (mPembayaran.status == NETRAL) {
                                                for (i in mPembayaran.barangSewa.indices) {
                                                    barangRef.child(mPembayaran.barangSewa[i].idBarang)
                                                        .child(STOCK_PATH).get()
                                                        .addOnSuccessListener { snapshot ->
                                                            val value = snapshot.getValue<Int>()
                                                            if (value != null) {
                                                                barangRef.child(mPembayaran.barangSewa[i].idBarang)
                                                                    .child(STOCK_PATH)
                                                                    .setValue(value + mPembayaran.barangSewa[i].jumlah)
                                                            }
                                                        }.addOnFailureListener { e ->
                                                            Toast.makeText(
                                                                activity,
                                                                e.message,
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                }
                                            } else if (mPembayaran.status == DITERIMA) {
                                                val ratingFragment = RatingFragment()
                                                val bundle = Bundle()
                                                bundle.putParcelableArrayList(
                                                    EXTRA_BARANG, mPembayaran.barangSewa
                                                )
                                                ratingFragment.show(
                                                    parentFragmentManager,
                                                    RatingFragment::class.java.simpleName
                                                )
                                                ratingFragment.arguments = bundle
                                            }

                                            it.ref.removeValue()
                                            storageBuktiRef.child("${mPembayaran.idPembayaran}.jpg")
                                                .delete()
                                        }.addOnFailureListener {
                                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG)
                                                .show()
                                        }
                                    dialog?.dismiss()
                                },
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .fillMaxWidth()
                                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = hapusText, textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            pembayaran = arguments?.getParcelable(EXTRA_PEMBAYARAN)
        }

        val barangSewa = pembayaran?.barangSewa
        if (barangSewa?.indices != null) {
            for (i in barangSewa.indices) {
                val keranjang = Keranjang()
                keranjang.idBarang = barangSewa[i].idBarang
                keranjang.namaBarang = barangSewa[i].namaBarang
                keranjang.hargaBarang = barangSewa[i].hargaBarang
                keranjang.jumlah = barangSewa[i].jumlah
                keranjang.subtotal = barangSewa[i].subtotal
                listKeranjang.add(keranjang)
            }
        }
    }


    private fun intentToWhatsApp(phone: String) {
        val packageManager = context?.packageManager
        val intent = Intent(Intent.ACTION_VIEW)

        if (packageManager != null) {
            try {
                val url = "https://api.whatsapp.com/send?phone=$phone"
                intent.setPackage("com.whatsapp")
                intent.data = Uri.parse(url)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    context?.startActivity(intent)
                } else {
                    if (intent.resolveActivity(packageManager) != null) {
                        context?.startActivity(intent)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }
}
