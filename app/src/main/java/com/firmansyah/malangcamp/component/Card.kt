package com.firmansyah.malangcamp.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.model.Barang
import com.firmansyah.malangcamp.other.ConstVariable.Companion.BARANG
import com.firmansyah.malangcamp.other.currencyIdrFormat
import com.firmansyah.malangcamp.other.rating
import com.firmansyah.malangcamp.screen.Screen
import com.firmansyah.malangcamp.theme.black
import com.firmansyah.malangcamp.theme.selectedCardColor


@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ItemBarangCard(
    barang: Barang,
    navController: NavHostController,
    pegawai: Boolean,
    changeCardColor: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        onClick = {
            if (pegawai) {
                navController.currentBackStackEntry?.savedStateHandle?.set(BARANG, barang)
                navController.navigate(Screen.BarangDetailScreen.route)
            } else {
                // TODO: Pindah ke Barang Detailnya Pelanggan
            }
        },
        backgroundColor = if (!pegawai && changeCardColor) selectedCardColor else MaterialTheme.colors.surface
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = barang.gambar,
                contentDescription = stringResource(id = R.string.gambar_barang),
                modifier = Modifier.width(120.dp),
                contentScale = ContentScale.Inside
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = barang.nama,
                    modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.stock),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = barang.stock.toString())
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = stringResource(R.string.gambar_rate),
                        modifier = Modifier.padding(start = 16.dp),
                        tint = Color.Yellow
                    )
                    Text(
                        text = stringResource(
                            id = R.string.rate_5,
                            rating(barang)
                        )
                    )
                }
                Divider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 8.dp),
                    color = black
                )
                Text(
                    text = stringResource(
                        id = R.string.rp,
                        currencyIdrFormat().format(barang.harga)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}