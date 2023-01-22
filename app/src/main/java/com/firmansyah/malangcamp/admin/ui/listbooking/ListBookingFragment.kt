package com.firmansyah.malangcamp.admin.ui.listbooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firmansyah.malangcamp.component.bookingItem
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//  Halaman daftar pelanggan yang memesan
class ListBookingFragment : Fragment() {

    private lateinit var listBookingViewModel: ListBookingViewModel
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listBookingViewModel =
            ViewModelProvider(this)[ListBookingViewModel::class.java]

        database = Firebase.database
        ref = database.getReference("pembayaran")

        auth = Firebase.auth

        return ComposeView(requireContext()).apply {
            setContent {
                MalangCampTheme {
                    ListBooking()
                }
            }
        }
    }

    @Composable
    private fun ListBooking() {
        listBookingViewModel.getListBooking(ref)
        val listPembayaran = listBookingViewModel.listBooking.value
        LazyColumn {
            bookingItem(listPembayaran, requireContext())
        }
    }
}

