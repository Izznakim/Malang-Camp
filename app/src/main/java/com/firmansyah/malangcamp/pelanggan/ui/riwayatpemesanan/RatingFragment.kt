package com.firmansyah.malangcamp.pelanggan.ui.riwayatpemesanan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Keranjang
import com.firmansyah.malangcamp.pelanggan.ui.barangsewa.DetailBarangSewaFragment.Companion.EXTRA_BARANG

//  Dialog rating
class RatingFragment : DialogFragment() {

    private lateinit var ratingViewModel: RatingViewModel

    private var barangSewa: ArrayList<Keranjang>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ratingViewModel =
            ViewModelProvider(this)[RatingViewModel::class.java]
        // Inflate the layout for this fragment

        return ComposeView(requireContext()).apply {
            setContent {
                val list by remember { mutableStateOf(listOf(1, 2, 3, 4, 5)) }
                var rating by rememberSaveable { mutableStateOf(0) }

                AlertDialog(
                    onDismissRequest = {
                        dialog?.dismiss()
                    },
                    title =
                    {
                        Text(
                            text = context.getString(R.string.beri_nilai_pada_barang_barang_yang_sudah_kamu_sewa),
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                list.forEach {
                                    Row {
                                        RadioButton(
                                            selected = rating == it,
                                            onClick = { rating = it }
                                        )
                                        Text(text = it.toString(), modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .clickable {
                                                rating = it
                                            })
                                    }
                                }
                            }
                            Text(
                                text = when (rating) {
                                    1 -> context.getString(R.string.sangat_kurang)
                                    2 -> context.getString(R.string.kurang)
                                    3 -> context.getString(R.string.cukup)
                                    4 -> context.getString(R.string.baik)
                                    5 -> context.getString(R.string.sangat_baik)
                                    else -> context.getString(R.string.belum_diberi_rating)
                                }, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                ratingViewModel.addRating(barangSewa, rating)
                                dialog?.dismiss()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                        ) {
                            Text(
                                text = stringResource(id = R.string.kirim),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }, dismissButton = {
                        Button(
                            onClick = {
                                dialog?.dismiss()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                        ) {
                            Text(
                                text = stringResource(R.string.tidak_terima_kasih),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                )

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            barangSewa = arguments?.getParcelableArrayList(EXTRA_BARANG)
        }

    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }
}