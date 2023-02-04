package com.firmansyah.malangcamp.pelanggan.ui.barangsewa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.component.ErrorFailComponent
import com.firmansyah.malangcamp.component.ItemBarangCard
import com.firmansyah.malangcamp.databinding.FragmentBarangsewaBinding
import com.firmansyah.malangcamp.theme.MalangCampTheme

//  Halaman daftar barang
class BarangSewaFragment : Fragment() {

    private lateinit var barangSewaViewModel: BarangSewaViewModel
    private var _binding: FragmentBarangsewaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        barangSewaViewModel =
            ViewModelProvider(this)[BarangSewaViewModel::class.java]

        _binding = FragmentBarangsewaBinding.inflate(inflater, container, false)
        return ComposeView(requireContext()).apply {
            setContent {
                val navController = rememberNavController()
                barangSewaViewModel.getListBarang()
                val listBarang = barangSewaViewModel.listBarang.value
                MalangCampTheme {
                    Box(modifier = Modifier.fillMaxSize()) {
                        when {
                            barangSewaViewModel.listBarang.value.isEmpty() -> {
                                ErrorFailComponent(
                                    Icons.Filled.Warning,
                                    stringResource(R.string.data_kosong_di_list_barang),
                                    stringResource(R.string.masih_belum_ada_barang_yang_ditambahkan)
                                )
                            }
                            barangSewaViewModel.isError.value -> {
                                ErrorFailComponent(
                                    icons = Icons.Filled.Close,
                                    contentDesc = barangSewaViewModel.errorMsg.value,
                                    textFail = barangSewaViewModel.errorMsg.value
                                )
                            }
                            else -> {
                                LazyColumn {
                                    items(items = listBarang, itemContent = { barang ->
                                        barangSewaViewModel.changeCardColor(barang)
                                        ItemBarangCard(
                                            barang,
                                            navController,
                                            false,
                                            barangSewaViewModel.changedColor.value
                                        )
                                    })
                                }
                            }
                        }
                        if (barangSewaViewModel.isLoading.value) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}