package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

//  Halaman daftar barang
class InformasiBarangFragment : Fragment() {

    private lateinit var informasiBarangViewModel: InformasiBarangViewModel
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        informasiBarangViewModel =
            ViewModelProvider(this)[InformasiBarangViewModel::class.java]

        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference("images/")

        database = Firebase.database
        databaseRef = database.getReference("barang")

        return ComposeView(requireContext()).apply {
            setContent {
                MalangCampTheme {
                    InformasiBarang()
                }
            }
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun InformasiBarang() {
        informasiBarangViewModel.getListBarang(databaseRef)
        val listBarang = informasiBarangViewModel.listBarang.value

        Scaffold(floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
            FabAddBarang(childFragmentManager)
        }, isFloatingActionButtonDocked = true) {
            LazyColumn {
                items(items = listBarang, itemContent = { barang ->
                    Box(contentAlignment = Alignment.TopStart) {
                        ItemBarangCard(barang, requireContext())
                        DeleteBarangButton(barang, databaseRef)
                        DeleteDialog(barang, databaseRef, storageRef, informasiBarangViewModel)
                    }
                })
            }
        }
    }

    @Preview
    @Composable
    fun InformasiBarangPreview() {
        MalangCampTheme {
            InformasiBarang()
        }
    }
}