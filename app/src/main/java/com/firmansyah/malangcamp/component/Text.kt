package com.firmansyah.malangcamp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.R
import com.firmansyah.malangcamp.theme.green

@Composable
fun LoginAdmin() {
    Text(
        text = stringResource(id = R.string.login_admin),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp),
        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
        color = green, textAlign = TextAlign.Center
    )
}