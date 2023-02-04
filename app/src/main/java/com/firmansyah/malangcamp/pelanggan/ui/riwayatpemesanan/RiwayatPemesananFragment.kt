package com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.ErrorFailComponent
import com.firmansyah.malangcamp.component.bookingItem
import com.firmansyah.malangcamp.databinding.FragmentRiwayatpemesananBinding
import com.firmansyah.malangcamp.theme.MalangCampTheme

//  Halaman daftar riwayat
class RiwayatPemesananFragment : Fragment() {

    private lateinit var riwayatPemesananViewModel: RiwayatPemesananViewModel

    private var _binding: FragmentRiwayatpemesananBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        riwayatPemesananViewModel =
            ViewModelProvider(this)[RiwayatPemesananViewModel::class.java]
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = rememberNavController()
                val context = LocalContext.current
                riwayatPemesananViewModel.getListRiwayat()
                MalangCampTheme {
                    Box(modifier = Modifier.fillMaxSize()) {
                        when {
                            riwayatPemesananViewModel.listRiwayat.value.isEmpty() -> {
                                ErrorFailComponent(
                                    Icons.Filled.Warning,
                                    stringResource(R.string.data_kosong_di_list_booking),
                                    stringResource(R.string.masih_belum_ada_yang_menyewa)
                                )
                            }
                            riwayatPemesananViewModel.isError.value -> {
                                ErrorFailComponent(
                                    icons = Icons.Filled.Close,
                                    contentDesc = riwayatPemesananViewModel.errorMsg.value,
                                    textFail = riwayatPemesananViewModel.errorMsg.value
                                )
                            }
                            else -> ListRiwayat(navController, riwayatPemesananViewModel, context)
                        }
                        if (riwayatPemesananViewModel.isLoading.value) {
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
        }
    }

    @Composable
    fun ListRiwayat(
        navController: NavHostController,
        riwayatPemesananViewModel: RiwayatPemesananViewModel,
        context: Context
    ) {
        val listPembayaran = riwayatPemesananViewModel.listRiwayat.value

        LazyColumn {
            bookingItem(listPembayaran, navController, context, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}