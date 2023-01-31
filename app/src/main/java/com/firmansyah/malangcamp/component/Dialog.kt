package com.firmansyah.malangcamp.component

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.firmansyah.malangcamp.admin.ui.informasibarang.ListBarangViewModel
import com.firmansyah.malangcamp.model.Barang
import com.google.firebase.database.DatabaseReference


@Composable
fun DeleteDialog(
    barang: Barang,
    databaseRef: DatabaseReference,
    listBarangViewModel: ListBarangViewModel
) {
    if (barang.delete) {
        AlertDialog(
            onDismissRequest = {
                databaseRef.child(barang.id).child("delete").setValue(false)
            },
            title =
            { Text(text = "DELETE!", fontWeight = FontWeight.Bold) },
            text = {
                Text(text = buildAnnotatedString {
                    append("Apakah kamu yakin ingin menghapus barang ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(
                            barang.nama
                        )
                    }
                    append("?")
                })
            },
            confirmButton = {
                Button(
                    onClick = {
                        listBarangViewModel.deleteBarang(
                            databaseRef,
                            barang
                        )
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Hapus barang ${barang.nama}"
                    )
                }
            }, dismissButton = {
                Button(
                    onClick = {
                        databaseRef.child(barang.id).child("delete")
                            .setValue(false)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Jangan hapus barang ${barang.nama}"
                    )
                }
            }
        )
    }
}