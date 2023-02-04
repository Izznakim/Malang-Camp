package com.firmansyah.malangcamp.screen.pegawai.ui.listbooking

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
fun ListBookingScreen(
    navController: NavHostController,
    listBookingViewModel: ListBookingViewModel = viewModel()
) {
    val context = LocalContext.current
    listBookingViewModel.getListBooking()
    MalangCampTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                listBookingViewModel.listBooking.value.isEmpty() -> {
                    ErrorFailComponent(
                        Icons.Filled.Warning,
                        stringResource(R.string.data_kosong_di_list_booking),
                        stringResource(R.string.masih_belum_ada_yang_menyewa)
                    )
                }
                listBookingViewModel.isError.value -> {
                    ErrorFailComponent(
                        icons = Icons.Filled.Close,
                        contentDesc = listBookingViewModel.errorMsg.value,
                        textFail = listBookingViewModel.errorMsg.value
                    )
                }
                else -> ListBooking(navController, listBookingViewModel, context)
            }
            if (listBookingViewModel.isLoading.value) {
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
private fun ListBooking(
    navController: NavHostController,
    listBookingViewModel: ListBookingViewModel,
    context: Context
) {
    val listPembayaran = listBookingViewModel.listBooking.value

    LazyColumn {
        bookingItem(listPembayaran, navController, context, true)
    }
}