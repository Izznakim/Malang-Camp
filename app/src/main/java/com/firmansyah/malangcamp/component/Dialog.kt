package com.firmansyah.malangcamp.component

import android.content.Context
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.DELETE_PATH
import com.firmansyah.malangcamp.screen.pegawai.ui.informasibarang.ListBarangViewModel
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun DeleteDialog(
    barang: Barang,
    databaseRef: DatabaseReference,
    listBarangViewModel: ListBarangViewModel,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    context: Context
) {
    if (barang.delete) {
        AlertDialog(
            onDismissRequest = {
                databaseRef.child(barang.id).child(DELETE_PATH).setValue(false)
            },
            title =
            { Text(text = stringResource(R.string.delete), fontWeight = FontWeight.Bold) },
            text = {
                Text(text = buildAnnotatedString {
                    append(stringResource(R.string.apakah_kamu_yakin_ingin_menghapus_barang))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(
                            barang.nama
                        )
                    }
                    append(stringResource(R.string._tanda_tanya_))
                })
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            listBarangViewModel.deleteBarang(
                                databaseRef,
                                barang.id,
                                context.getString(R.string.barang_id___jpg, barang.id)
                            )
                            listBarangViewModel.getMsg(
                                context.getString(
                                    R.string.barang_nama__telah_dihapus,
                                    barang.nama
                                )
                            )
                            listBarangViewModel.msg.also {
                                scaffoldState.snackbarHostState.showSnackbar(message = it)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(R.string.hapus_barang_, barang.nama)
                    )
                }
            }, dismissButton = {
                Button(
                    onClick = {
                        databaseRef.child(barang.id).child(DELETE_PATH)
                            .setValue(false)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(
                            R.string.jangan_hapus_barang,
                            barang.nama
                        )
                    )
                }
            }
        )
    }
}