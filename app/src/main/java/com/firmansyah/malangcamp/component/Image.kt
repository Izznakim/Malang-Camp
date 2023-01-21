package com.firmansyah.malangcamp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.R

@Composable
fun LogoMalangCamp() {
    Image(
        painter = painterResource(id = R.drawable.logo_mc),
        contentDescription = stringResource(id = R.string.logo_malang_camp),
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(226.dp)
    )
}

@Composable
fun LogoLoginAdmin() {
    Image(
        painter = painterResource(id = R.drawable.mountains),
        contentDescription = stringResource(id = R.string.login_admin),
        contentScale = ContentScale.Fit,
        modifier = Modifier.size(146.dp)
    )
}
