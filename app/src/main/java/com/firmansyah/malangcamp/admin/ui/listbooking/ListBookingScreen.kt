package com.firmansyah.malangcamp.admin.ui.listbooking

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.firmansyah.malangcamp.component.bookingItem
import com.firmansyah.malangcamp.theme.MalangCampTheme

@Composable
fun ListBookingScreen(
    navController: NavHostController,
    listBookingViewModel: ListBookingViewModel = viewModel()
) {
    MalangCampTheme {
        ListBooking(navController, listBookingViewModel)
    }
}

@Composable
private fun ListBooking(
    navController: NavHostController,
    listBookingViewModel: ListBookingViewModel
) {
    listBookingViewModel.getListBooking()
    val listPembayaran = listBookingViewModel.listBooking.value

    LazyColumn {
        bookingItem(listPembayaran, navController)
    }
}