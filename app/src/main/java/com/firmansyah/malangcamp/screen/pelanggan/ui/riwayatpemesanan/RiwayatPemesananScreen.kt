package com.firmansyah.malangcamp.screen.pelanggan.ui.riwayatpemesanan

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
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
import com.firmansyah.malangcamp.component.ErrorFailComponent
import com.firmansyah.malangcamp.component.bookingItem
import com.firmansyah.malangcamp.theme.MalangCampTheme

@Composable
fun RiwayatPemesananScreen(
    navController: NavHostController,
    viewModel: RiwayatPemesananViewModel = viewModel()
) {
    val context = LocalContext.current
    viewModel.getListRiwayat()
    MalangCampTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                viewModel.listRiwayat.value.isEmpty() -> {
                    ErrorFailComponent(
                        Icons.Filled.Warning,
                        stringResource(R.string.data_kosong_di_list_booking),
                        stringResource(R.string.masih_belum_ada_yang_menyewa)
                    )
                }
                viewModel.isError.value -> {
                    ErrorFailComponent(
                        icons = Icons.Filled.Close,
                        contentDesc = viewModel.errorMsg.value,
                        textFail = viewModel.errorMsg.value
                    )
                }
                else -> ListRiwayat(navController, viewModel, context)
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

@Composable
fun ListRiwayat(
    navController: NavHostController,
    viewModel: RiwayatPemesananViewModel,
    context: Context
) {
    val listPembayaran = viewModel.listRiwayat.value

    LazyColumn {
        bookingItem(listPembayaran, navController, context, false)
    }
}