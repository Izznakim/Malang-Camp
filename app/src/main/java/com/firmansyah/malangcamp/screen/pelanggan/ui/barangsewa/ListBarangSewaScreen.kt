package com.firmansyah.malangcamp.screen.pelanggan.ui.barangsewa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.ErrorFailComponent
import com.firmansyah.malangcamp.component.ItemBarangCard
import com.firmansyah.malangcamp.theme.MalangCampTheme

@Composable
fun ListBarangSewaScreen(
    navController: NavHostController,
    viewModel: ListBarangSewaViewModel = viewModel()
) {
    viewModel.getListBarang()
    val listBarang = viewModel.listBarang.value
    MalangCampTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                viewModel.listBarang.value.isEmpty() -> {
                    ErrorFailComponent(
                        Icons.Filled.Warning,
                        stringResource(R.string.data_kosong_di_list_barang),
                        stringResource(R.string.masih_belum_ada_barang_yang_ditambahkan)
                    )
                }
                viewModel.isError.value -> {
                    ErrorFailComponent(
                        icons = Icons.Filled.Close,
                        contentDesc = viewModel.errorMsg.value,
                        textFail = viewModel.errorMsg.value
                    )
                }
                else -> {
                    LazyColumn {
                        items(items = listBarang, itemContent = { barang ->
                            viewModel.changeCardColor(barang)
                            ItemBarangCard(
                                barang,
                                navController,
                                false,
                                viewModel.changedColor.value
                            )
                        })
                    }
                }
            }
            if (viewModel.isLoading.value) {
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