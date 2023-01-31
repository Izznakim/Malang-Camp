package com.firmansyah.malangcamp.admin.ui.informasibarang

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.component.DeleteBarangButton
import com.firmansyah.malangcamp.component.DeleteDialog
import com.firmansyah.malangcamp.component.FabAddBarang
import com.firmansyah.malangcamp.component.ItemBarangCard
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun ListBarangScreen(
    navController: NavHostController,
    listBarangViewModel: ListBarangViewModel = viewModel()
) {
    MalangCampTheme {
        InformasiBarang(navController, listBarangViewModel)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun InformasiBarang(
    navController: NavHostController,
    listBarangViewModel: ListBarangViewModel
) {
    val databaseRef = Firebase.database.getReference("barang")
    listBarangViewModel.getListBarang(databaseRef)
    val listBarang = listBarangViewModel.listBarang.value

    Scaffold(floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
        FabAddBarang(navController)
    }, isFloatingActionButtonDocked = true) {
        LazyColumn {
            items(items = listBarang, itemContent = { barang ->
                Box(contentAlignment = Alignment.TopStart) {
                    ItemBarangCard(barang, navController)
                    DeleteBarangButton(barang, databaseRef)
                    DeleteDialog(barang, databaseRef, listBarangViewModel)
                }
            })
        }
    }
}