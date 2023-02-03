package com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.*
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.theme.MalangCampTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope

@Composable
fun ListBarangScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    viewModel: ListBarangViewModel = viewModel()
) {
    MalangCampTheme {
        InformasiBarang(
            navController,
            scaffoldState,
            coroutineScope, viewModel
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun InformasiBarang(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    listBarangViewModel: ListBarangViewModel
) {
    val databaseRef = Firebase.database.getReference(BARANG)
    listBarangViewModel.getListBarang(databaseRef)
    val listBarang = listBarangViewModel.listBarang.value
    val context = LocalContext.current

    Scaffold(floatingActionButton = {
        FabAddBarang(navController)
    }, isFloatingActionButtonDocked = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                listBarangViewModel.listBarang.value.isEmpty() -> {
                    ErrorFailComponent(
                        Icons.Filled.Warning,
                        stringResource(R.string.data_kosong_di_list_barang),
                        stringResource(R.string.masih_belum_ada_barang_yang_ditambahkan)
                    )
                }
                listBarangViewModel.isError.value -> {
                    ErrorFailComponent(
                        icons = Icons.Filled.Close,
                        contentDesc = listBarangViewModel.errorMsg.value,
                        textFail = listBarangViewModel.errorMsg.value
                    )
                }
                else -> {
                    LazyColumn {
                        items(items = listBarang, itemContent = { barang ->
                            Box(contentAlignment = Alignment.TopStart) {
                                ItemBarangCard(barang, navController, pegawai = true, false)
                                DeleteBarangButton(barang, databaseRef)
                                DeleteDialog(
                                    barang,
                                    databaseRef,
                                    listBarangViewModel,
                                    scaffoldState,
                                    coroutineScope,
                                    context
                                )
                            }
                        })
                    }
                }
            }
            if (listBarangViewModel.isLoading.value) {
                CircularProgressIndicator(
                    Modifier
                        .alpha(1f)
                        .align(Alignment.Center)
                )
            } else {
                CircularProgressIndicator(
                    Modifier
                        .alpha(0f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}